import java.util.ArrayList;

public class DataHandler {
    ArrayList<String[]> data = new ArrayList<>();

    private DataHandler() {}

    public DataHandler createDataHandler() {
        return new DataHandler();
    }

    public void saveMessage(String[] dialogue){
        data.add(dialogue);
    }

    public String[] getPrevious() {
        return data.get(-2);
    }

    public String[][] getAllMessages() {
        String[][] ret = new String[data.size()][];
        int count = 0;
        for (String[] s : data) {
            ret[count] = s;
            count++;
        }
        return ret;

    }
}
