# RISC-V Emulator

This project is a RISC-V emulator implemented in Java for the COSC 530 Summer Project. It simulates a basic RISC-V machine and supports various instruction types, memory operations, and system calls.

## Project Structure

- **src/main/java/**: Contains the source code for the emulator.
- **bin/**: Directory where the compiled `.class` files will be placed.
- **README.md**: This file.

## Prerequisites

- JDK 17 installed.
- A CLI to run commands for the program.

## Compilation

To compile the project, navigate to the root directory of the project where the `src` directory is located. Use the following command to compile all Java files:

```bash
javac -d bin $(Get-ChildItem -Recurse -Filter *.java | Select-Object -ExpandProperty FullName)
```

This command will compile all `.java` files in the `src/main/java` directory and place the `.class` files in the bin directory.

## Running the Emulator

After compiling the class files, you can run the emulator by running this command:

```bash
java -cp bin org.parkerharrelson.riscv.Emulator <path_to_ELF_file>
```

## System Calls Supported
- exit: Exits the virtual machine (the run method in the Machine class will loop until there is an exit call)
- putchar: Outputs a character to the standard output
- getchar: reads a character from the standard input
- debug: not actually implemented, but could be enabled for debugging purposes

