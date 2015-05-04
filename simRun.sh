# Do the port forwarding to remote debug
# My 7070 is big's 6969
ssh -nTNfR 6969:localhost:7070 big

# Go to the execution folder
cd ~/spring-dev/springSimulator

# Pull new changes
git pull

# Empty the compiled classes directory
rm -rf bin
mkdir bin

# Compile the classes needed to launch the simulation
javac -g -d bin -sourcepath "src" -classpath "lib/simgrid-3.11.1.jar" $(find src -name "*.java")

# Run the simulation
# The experimental coroutine JVM may be faster
# Debug activated (jdwp) waiting for debugger to connect
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=7070 -classpath "lib/simgrid-3.11.1.jar:bin" springSimulator.SimulatorTest
