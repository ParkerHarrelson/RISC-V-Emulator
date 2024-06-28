# verify_memory.py

with open('expected_memory.bin', 'rb') as f:
    contents = f.read()

for i, byte in enumerate(contents):
    print(f"{i:04x}: {byte:02x}")

# Ensure that the contents match what you expect
