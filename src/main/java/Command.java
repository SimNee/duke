import java.io.IOException;

public class Command {

    public Command() {
    }

    public void execute(TaskList taskList, UI ui,Storage storage) throws DukeException, IOException {
        storage.LoadFile();
        ui.printGreeting();
        String TaskLine = ui.readCommand();
        Parser parser = new Parser();
        outLoop:
        while (TaskLine != null) {
            String[] words = TaskLine.split(" ");
            switch (parser.parse(TaskLine)) {
            case BYE:
                ui.printBye();
                break outLoop;

            case LIST :
                ui.printList(taskList);
                TaskLine = ui.readCommand();
                break;

            case DONE :
                int taskNo = Integer.parseInt(TaskLine.substring(5));
                taskList.get(taskNo - 1).markAsDone(taskList.get(taskNo - 1));
                ui.printDone(taskList.get(taskNo - 1));
                TaskLine = ui.readCommand();
                break;

            case TODO :
                if (TaskLine.length() == 4) {
                    ui.throwInputError("todo");
                } else {
                    Task todoTask = new Todo(TaskLine.substring(5));
                    taskList.add(todoTask);
                    ui.printAddTask(todoTask,taskList.size());
                }
                TaskLine = ui.readCommand();

                break;

            case DEADLINE :
                if (TaskLine.length() == 8) {
                    ui.throwInputError("deadline");
                } else {
                    int indexOfSlash = TaskLine.indexOf("/");
                    Task deadlineTask = new Deadline(TaskLine.substring(9, indexOfSlash - 1), TaskLine.substring(indexOfSlash + 4));
                    taskList.add(deadlineTask);
                    ui.printAddTask(deadlineTask,taskList.size());
                }
                TaskLine = ui.readCommand();
                break;

            case EVENT :
                if (TaskLine.length() == 5) {
                    ui.throwInputError("event");
                } else {
                    int indexOfSlash = TaskLine.indexOf("/");
                    Task eventTask = new Event(TaskLine.substring(6, indexOfSlash - 1), TaskLine.substring(indexOfSlash + 4));
                    taskList.add(eventTask);
                    ui.printAddTask(eventTask,taskList.size());
                }
                TaskLine = ui.readCommand();
                break;

            case DELETE :
                int taskNoDelete = Integer.parseInt(TaskLine.substring(7));
                Task deletedTask = taskList.get(taskNoDelete - 1);
                ui.printDelete(deletedTask, taskList.size());
                taskList.remove(taskNoDelete - 1);
                TaskLine = ui.readCommand();
                break;

            case FIND :
                String keyword = TaskLine.substring(5);
                TaskList findList = new TaskList();
                for (int i = 0; i < taskList.size(); i++) {
                    if (taskList.get(i).getDescription().contains(keyword)) {
                        findList.add(taskList.get(i));
                    }
                }
                ui.printFind(findList);
                TaskLine = ui.readCommand();
                break;

            default :
                ui.printIDK();
                TaskLine = ui.readCommand();
                break;
            }
        }
        storage.UpdateFile();
    }
}
