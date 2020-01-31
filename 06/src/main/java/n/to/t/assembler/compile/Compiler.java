package n.to.t.assembler.compile;

import n.to.t.assembler.instruction.Instruction;
import n.to.t.assembler.parse.Parser;
import n.to.t.assembler.parse.statement.Address;
import n.to.t.assembler.parse.statement.Compute;
import n.to.t.assembler.parse.statement.Label;
import n.to.t.assembler.source.Source;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static java.nio.file.Files.write;
import static java.util.stream.Collectors.toList;

public final class Compiler {

    public void compile(final Path sourcePath, final Path targetPath) throws IOException {
        final var source = Source.of(sourcePath);
        final var program = compile(source);
        final var output = program.instructions().stream().map(Instruction::toBitString).collect(toList());
        write(targetPath, output);
    }

    public Executable compile(final Source source) {
        final var parser = new Parser();
        final var symbolTable = firstPass(source, parser);
        return secondPass(source, parser, symbolTable);
    }

    private SymbolTable firstPass(final Source source, final Parser parser) {
        final var symbolTable = initializeSymbolTable();
        for (final Label label : parser.parseLabels(source)) {
            symbolTable.addLabel(label.symbol(), label.address());
        }
        return symbolTable;
    }

    private Executable secondPass(final Source source, final Parser parser, final SymbolTable symbolTable) {
        final var instructions = new ArrayList<Instruction>();
        for (final var statement : parser.parse(source)) {
            if (statement.is(Address.class)) {
                final var address = statement.as(Address.class);
                final var instruction = Instruction.a()
                        .withAddress(address.resolve(symbolTable));

                instructions.add(instruction);
            }
            if (statement.is(Compute.class)) {
                final var compute = statement.as(Compute.class);
                final var instruction = Instruction.c()
                        .withDest(compute.dest().map(this::destToBits).orElse(0b000))
                        .withComp(compToBits(compute.comp()))
                        .withJump(compute.jump().map(this::jumpToBits).orElse(0b000));

                instructions.add(instruction);
            }
        }
        return () -> instructions;
    }

    private int destToBits(final String dest) {
        return switch (dest) {
            case "M"   -> 0b001;
            case "D"   -> 0b010;
            case "MD"  -> 0b011;
            case "A"   -> 0b100;
            case "AM"  -> 0b101;
            case "AD"  -> 0b110;
            case "AMD" -> 0b111;
            default    -> throw new UnsupportedOperationException("Unknown dest: " + dest);
        };
    }

    private int compToBits(final String comp) {
        return switch (comp) {
            case "0"   -> 0b0_101010;
            case "1"   -> 0b0_111111;
            case "-1"  -> 0b0_111010;
            case "D"   -> 0b0_001100;
            case "A"   -> 0b0_110000;
            case "!D"  -> 0b0_001101;
            case "!A"  -> 0b0_110001;
            case "-D"  -> 0b0_001111;
            case "-A"  -> 0b0_110011;
            case "D+1" -> 0b0_011111;
            case "A+1" -> 0b0_110111;
            case "D-1" -> 0b0_001110;
            case "A-1" -> 0b0_110010;
            case "D+A" -> 0b0_000010;
            case "D-A" -> 0b0_010011;
            case "A-D" -> 0b0_000111;
            case "D&A" -> 0b0_000000;
            case "D|A" -> 0b0_010101;
            case "M"   -> 0b1_110000;
            case "!M"  -> 0b1_110001;
            case "-M"  -> 0b1_110011;
            case "M+1" -> 0b1_110111;
            case "M-1" -> 0b1_110010;
            case "D+M" -> 0b1_000010;
            case "D-M" -> 0b1_010011;
            case "M-D" -> 0b1_000111;
            case "D&M" -> 0b1_000000;
            case "D|M" -> 0b1_010101;
            case "M+D" -> 0b1_000010;
            case "A+D" -> 0b0_000010;
            case "M|D" -> 0b1_010101;
            case "M&D" -> 0b1_000000;
            default    -> throw new UnsupportedOperationException("Unknown comp: " + comp);
        };
    }

    private int jumpToBits(final String jump) {
        return switch (jump) {
            case "JGT" -> 0b001;
            case "JEQ" -> 0b010;
            case "JGE" -> 0b011;
            case "JLT" -> 0b100;
            case "JNE" -> 0b101;
            case "JLE" -> 0b110;
            case "JMP" -> 0b111;
            default    -> throw new UnsupportedOperationException("Unknown jump: " + jump);
        };
    }

    private static SymbolTable initializeSymbolTable() {
        return SymbolTable.withInitialVariableAddress(16)
                .addPredefined("R0", 0)
                .addPredefined("R1", 1)
                .addPredefined("R2", 2)
                .addPredefined("R3", 3)
                .addPredefined("R4", 4)
                .addPredefined("R5", 5)
                .addPredefined("R6", 6)
                .addPredefined("R7", 7)
                .addPredefined("R8", 8)
                .addPredefined("R9", 9)
                .addPredefined("R10", 10)
                .addPredefined("R11", 11)
                .addPredefined("R12", 12)
                .addPredefined("R13", 13)
                .addPredefined("R14", 14)
                .addPredefined("R15", 15)
                .addPredefined("SCREEN", 16384)
                .addPredefined("KBD", 24576)
                .addPredefined("SP", 0)
                .addPredefined("LCL", 1)
                .addPredefined("ARG", 2)
                .addPredefined("THIS", 3)
                .addPredefined("THAT", 4);
    }
}
