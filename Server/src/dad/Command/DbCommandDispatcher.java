package dad.Command;

import dad.server.DBServer;


public class DbCommandDispatcher implements CommandDispatcher {
    private DBServer server;

    public DbCommandDispatcher(DBServer server)
    {
        this.server = server;
    }

    @Override
    public String send(Command command) {
        if (command instanceof DbCommand) {
            return this.server.execute((DbCommand) command);
         }else {
            return command.execute();
         }
    }
}
