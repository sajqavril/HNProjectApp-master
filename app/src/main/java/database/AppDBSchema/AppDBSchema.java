package database.AppDBSchema;

public class AppDBSchema {
    public static final class ChatTable{
        public static final String NAME = "Chats";
        //可以通过ChatTable.Cols.UUID来查看chat的uuid
        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String CHATNAME = "chatName";
            public static final String CHATFACE = "chatFace";
            public static final String NOTIFY = "ifNotify";
            public static final String CHATMEMBERS = "chatMembers";
            public static final String CHATMESSAGES = "chatMessages";
        }
    }
}
