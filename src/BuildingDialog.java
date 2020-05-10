import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Created by ansleighboateng on 3/19/2017.
 */
public class BuildingDialog extends Dialog {
    public BuildingDialog(Stage building, Building b ){
        setTitle("Building Overview");
        int rooms = 0;
        int size = 0;
        int exit = 0;
        update();
        for(int i=0; i<b.getFloorPlans().length;i++) {
            rooms=rooms+b.getFloorPlan(i).getNumberOfRooms();
            size=size+b.getFloorPlan(i).size();
            for (int x = 0; x < b.getFloorPlan(i).size(); x++) {
                for (int y = 0; y < b.getFloorPlan(i).size(); y++) {
                    if (b.hasExitAt(x, y)) {
                        exit++;
                    }
                }
            }
        }

        Button list =  new Button("Directory Listing");
        getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(20);
        grid.setPadding(new Insets(10,10,10,10));
        grid.add(new Label("Num Floors:"), 0, 0);
        grid.add(new Label("Num Exits:"), 0, 1);
        grid.add(new Label("Num Rooms:"), 0, 2);
        grid.add(new Label("Total Size:"), 0, 3);
        TextField nf = new TextField();
        grid.add(nf,1,0);
        TextField ne = new TextField();
        grid.add(ne,1,1);
        TextField nr = new TextField();
        grid.add(nr,1,2);
        TextField ts = new TextField();
        grid.add(ts,1,3);
        grid.add(list,0,4);
        nf.setEditable(false);
        ne.setEditable(false);
        ts.setEditable(false);
        nr.setEditable(false);
        nf.setText(""+b.getFloorPlans().length);
        ne.setText(""+exit);
        nr.setText(""+rooms);
        ts.setText(size+"Sq. Ft");
        getDialogPane().setContent(grid);
        update();

        list.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Dialog dialog = new DirectoryDialog(b);
                dialog.showAndWait();
            }
        });
    }
    public void update() {
        int rooms = 0;
        int size = 0;
        int exit = 0;
    }
}

