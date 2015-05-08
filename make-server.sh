javac ./src/taki/common/ChatMessage.java ./src/taki/server/Main.java ./src/taki/server/ServerGUI.java ./src/taki/server/Server.java -d ./bin/server/class
jar cvf ./bin/server/server.jar ./bin/server/class/taki
