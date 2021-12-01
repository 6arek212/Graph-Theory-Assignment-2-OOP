package ui;

public class UIEvents {
    public static class ShowMessage extends UIEvents {
        private String message;

        public ShowMessage(String message) {
            super();
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
