public class Main {

    public static void main(String[] args) {

        Static.setCommandList();
        Sql sql = new Sql();
        while (true){
            String command;
            if (Static.commandList.contains(command = Static.inS())){
                switch (command){
                    case "new":
                        sql.sqlNew();
                        break;
                    case "apply":
                        sql.sqlApply();
                        break;
                }
            } else {
                System.out.println("There is no such a command");
            }
        }
    }
}
