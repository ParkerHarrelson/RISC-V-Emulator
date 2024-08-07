package org.parkerharrelson.riscv;

import org.parkerharrelson.riscv.core.Machine;
import java.io.IOException;

/**
 * Main entry point for the RISC-V emulator.
 * This class is responsible for pulling the command line arguments
 * out and instantiating the Machine itself. It does some basic validation
 * and exception handling to ensure that we can actually run the emulator.
 * It is also responsible for telling the machine when to run.
 */
public class Emulator {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Error: Must Provide File Path");
            System.exit(1);
        }

        Machine machine = new Machine();
        String filePath = args[0];

        try {
            machine.loadContents(filePath);
            machine.run();
        } catch (IOException e) {
            System.err.println("Error reading ELF File: " + e.getMessage());
            System.exit(1);
        }
    }
}
