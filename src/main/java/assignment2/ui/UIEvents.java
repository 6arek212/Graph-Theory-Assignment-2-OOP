package assignment2.ui;

public class UIEvents {
    public static class UpdateUi extends UIEvents {

    }


    public static class Labels extends UIEvents {
        private int numberOfNode;
        private int numberOfEdges;

        public Labels(int numberOfNode, int numberOfEdges) {
            this.numberOfNode = numberOfNode;
            this.numberOfEdges = numberOfEdges;
        }


        public int getNumberOfNode() {
            return numberOfNode;
        }

        public int getNumberOfEdges() {
            return numberOfEdges;
        }
    }

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
