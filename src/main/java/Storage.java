import java.io.*;

/**
 * Storing of the task list in a text file which is accessible for future running of the code
 */
public class Storage {

    protected TaskList taskList;
    protected File taskListText;

    public Storage(TaskList taskList){
        this.taskList = taskList;
    }

    /**
     * Loading in the file to the chatbot to digest
     * @throws IOException
     */
    public void LoadFile() throws IOException {
        try {
            CheckFile();
            taskListText = new File("TaskList.txt");
            BufferedReader TasksFile = new BufferedReader(new FileReader(taskListText));
            String LineFile = "";
            while ((LineFile = TasksFile.readLine()) != null) {
                String[] WordsFile = LineFile.split("`");
                switch (WordsFile[0]) {
                case ("todo"):
                    Task todoFile = new Todo(WordsFile[2]);
                    taskList.add(todoFile);
                    if (WordsFile[1].equals("\u2713")) {
                        todoFile.markAsDone(todoFile);
                    }
                    break;

                case ("event"):
                    Task eventFile = new Event(WordsFile[2], WordsFile[3]);
                    taskList.add(eventFile);
                    if (WordsFile[1].equals("\u2713")) {
                        eventFile.markAsDone(eventFile);
                    }
                    break;

                case ("deadline"):
                    Task deadlineFile = new Deadline(WordsFile[2], WordsFile[3]);
                    taskList.add(deadlineFile);
                    if (WordsFile[1].equals("\u2713")) {
                        deadlineFile.markAsDone(deadlineFile);
                    }
                    break;

                default:
                    System.out.println("error");
                }
            }
        } catch (FileNotFoundException e) {

        }
    }

    /**
     * Updating the file and saving it remotely for future references
     * @throws IOException
     */
    public void UpdateFile() throws IOException {
        try (PrintStream out = new PrintStream(new FileOutputStream(taskListText))) {
            for (int i = 0; i < taskList.size(); i++) {
                Task t = taskList.get(i);
                if (t.getType().equals("todo")) {
                    out.print(t.getType() + "`" + t.getStatusIcon() + "`" + t.getDescription() + "`" + "\n" );
                } else {
                    out.print(t.getType() + "`" + t.getStatusIcon() + "`" + t.getDescription() + "`" + t.getDate() + "\n" );
                }
            }
        }
    }

    /**
     * Checking if the file already exist or not. If file does not exist, it creates an empty file
     * @throws IOException
     */
    public void CheckFile() throws IOException {
        File tmpDir = new File("TaskList.txt");
        if (!tmpDir.exists()) {
            tmpDir.createNewFile();
        }
    }
}