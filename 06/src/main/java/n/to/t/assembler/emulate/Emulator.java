package n.to.t.assembler.emulate;

import n.to.t.assembler.compile.Executable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.SOUTH;
import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import static java.awt.event.KeyEvent.*;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.Thread.sleep;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public final class Emulator {

    private static final int ROM_DATA_SIZE = 32768;
    private static final int RAM_DATA_SIZE = 16384;
    private static final int SCREEN_DATA_SIZE = 8192;
    private static final int KEYBOARD_DATA_SIZE = 1;
    private static final int RAM_SIZE = RAM_DATA_SIZE + SCREEN_DATA_SIZE + KEYBOARD_DATA_SIZE;

    private static final int SCREEN_BASE = 16384;
    private static final int KEYBOARD_BASE = 24576;

    private static final int INSTRUCTIONS_PER_TICK = 1 << 12;

    private final JFrame ui;

    private int[] ram;
    private int a;
    private int d;
    private int pc;
    private volatile boolean reset;

    public Emulator() {
        final var reset = new Reset();
        final var keyboard = new Keyboard();
        final var screen = new Screen(512, 256);

        this.ui = initUI(reset, keyboard, screen);
    }

    private JFrame initUI(final Reset reset, final Keyboard keyboard, final Screen screen) {
        final var ui = new JFrame();
        ui.setSize(512, 256 + 128);
        ui.setResizable(false);
        ui.setLayout(new BorderLayout());
        ui.add(screen, CENTER);
        ui.add(reset, SOUTH);
        ui.setLocationRelativeTo(null);
        ui.setVisible(true);
        ui.setDefaultCloseOperation(EXIT_ON_CLOSE);
        ui.addKeyListener(keyboard);
        ui.requestFocusInWindow();
        return ui;
    }

    private void reset() {
        reset = true;
        ram = new int[RAM_SIZE];
        a = 0;
        d = 0;
        pc = 0;
        ui.requestFocusInWindow();
        reset = false;
    }

    public void run(final Executable executable) throws InterruptedException {
        reset();

        final var rom = new int[ROM_DATA_SIZE];
        var address = 0;
        for (final var instruction : executable.instructions()) {
            rom[address++] = instruction.bits();
        }
        emulate(rom);
    }

    private void emulate(final int[] rom) throws InterruptedException {
        while (true) {
            for (var i = 0; i < INSTRUCTIONS_PER_TICK && !reset; i++) {
                execute(rom[pc++]);
            }
            ui.repaint();
            sleep(1);
        }
    }

    private void execute(final int instruction) {
        if ((instruction & 0x8000) == 0) {
            a = instruction;
            return;
        }

        final var controls = (instruction >>> 6);

        var x = d;
        var y = ((instruction >>> 12) & 1) == 0 ? a : ram[a];
        var out = 0;

        x = (controls & 0b100000) == 0 ? x : 0;
        x = (controls & 0b010000) == 0 ? x : ~x;
        y = (controls & 0b001000) == 0 ? y : 0;
        y = (controls & 0b000100) == 0 ? y : ~y;

        out = (controls & 0b000010) == 0 ? x & y : x + y;
        out = (controls & 0b000001) == 0 ? out : ~out;
        out = (short) out;

        final var dest = instruction >>> 3;
        if ((dest & 0b001) != 0) {
            ram[a] = out;
        }
        if ((dest & 0b010) != 0) {
            d = out;
        }
        if ((dest & 0b100) != 0) {
            a = out;
        }

        final var jump = instruction & 0b111;
        if (((jump & 0b001) != 0 && out > 0) || ((jump & 0b010) != 0 && out == 0) || ((jump & 0b100) != 0 && out < 0)) {
            pc = a;
        }
    }

    final class Screen extends JPanel {

        private final BufferedImage image;
        private final int[] pixels;

        Screen(final int width, final int height) {
            this.image = new BufferedImage(width, height, TYPE_INT_RGB);
            this.pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        }

        @Override
        public void paintComponent(final Graphics g) {
            super.paintComponent(g);
            final var graphics = (Graphics2D) g;
            draw(ram, pixels);
            graphics.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        }

        private void draw(final int[] ram, final int[] pixels) {
            for (int address = 0; address < SCREEN_DATA_SIZE; address++) {
                final var word = ram[SCREEN_BASE + address];
                for (int bit = 0; bit < 16; bit++) {
                    final var pixel = word >>> bit;
                    pixels[address * 16 + bit] = (pixel & 1) == 0 ? WHITE.getRGB() : BLACK.getRGB();
                }
            }
        }
    }

    final class Reset extends JPanel {

        Reset() {
            final var button = new JButton("Reset");
            button.addActionListener(__ -> Emulator.this.reset());
            add(button);
        }
    }

    final class Keyboard implements KeyListener {

        private final int keyboardAddress = KEYBOARD_BASE;

        @Override
        public void keyTyped(final KeyEvent event) {
        }

        @Override
        public void keyPressed(final KeyEvent event) {
            final var code = event.getKeyCode();
            final var key = event.getKeyChar();
            ram[keyboardAddress] = switch (code) {
                case VK_ENTER ->      128;
                case VK_BACK_SPACE -> 129;
                case VK_LEFT ->       130;
                case VK_UP ->         131;
                case VK_RIGHT ->      132;
                case VK_DOWN ->       133;
                case VK_ESCAPE ->     140;
                default -> key; // don't care about 'might not be meaningful'
            };
        }

        @Override
        public void keyReleased(final KeyEvent event) {
            ram[keyboardAddress] = 0; // assume single key press at any time
        }
    }
}
