## Task manager application


#### How to run project

Clone repo:
`git clone https://github.com/fedran/task-manager.git`

Enter to project directory:
`cd ./task-manager`

Build project:
`./mvnw clean package`

Run :
`java -jar ./target/task-manager.jar`

Or run in **debug** mode :
`java -jar -Dspring.profiles.active="debug" ./target/task-manager.jar`
