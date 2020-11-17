package miniCAD;

public class Main {

    public static void main(String[] args) {
        CadFrame cadFrame = new CadFrame();
        cadFrame.initInterface();

        Controller.setCadFrame(cadFrame);
        Controller.addListener();
    }

}
