import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
//import javafx.scene.chart.AreaChartBuilder;
import javafx.scene.control.Dialog;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.*;

import java.util.Optional;

/**
 * Created by ansleighboateng on 3/19/2017.
 */
public class DirectoryDialog extends Dialog {
    public DirectoryDialog(Building b) {
        setTitle("Directory Listing");
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        Button search = new Button("Search");
        ListView<String> peeps = new ListView<String>();
        search.setPrefWidth(450);
        Room[] room = new Room[60];
        String[] rooms = new String[60];
        int index=0;
        ArrayList sections=new ArrayList<String>();
        Set<String> duplicates = new HashSet<>();


        for (int i=0; i<b.getFloorPlans().length;i++){
            for (int x= 0; x < b.getFloorPlan(i).size(); x++) {
                for (int y = 0; y < b.getFloorPlan(i).size(); y++) {
                    if (b.getFloorPlan(i).roomAt(x, y) != null) {
                        duplicates.addAll(sections);
                        sections.clear();
                        sections.addAll(duplicates);
                        sections.add(""+b.getFloorPlan(i).roomAt(x,y).getNumber()+" - "+b.getFloorPlan(i).roomAt(x,y).getOccupant()+" ("+b.getFloorPlan(i).roomAt(x,y).getPosition()+")");
                    }
                }
            }
        }
        peeps.setItems(FXCollections.observableArrayList(sections));
        grid.add(peeps,0,0);
        grid.add(search,0,1);

        getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        getDialogPane().setContent(grid);

        search.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                TextInputDialog searchText = new TextInputDialog();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                searchText.setTitle("Input Required");
                searchText.setHeaderText(null);
                searchText.setContentText("Please enter the full name of the person that you are searching for:");
                Optional<String> result = searchText.showAndWait();
                String input = result.get();
                if(result.isPresent()){
                    for (int i=0;i<b.getFloorPlans().length;i++){
                        for (int x = 0; x < b.getFloorPlan(i).size(); x++) {
                            for (int y = 0; y < b.getFloorPlan(i).size(); y++) {
                                if (b.getFloorPlan(i).roomAt(x, y) != null) {
                                    if (input.equals(b.getFloorPlan(i).roomAt(x,y).getOccupant())) {
                                        alert.setContentText(b.getFloorPlan(i).roomAt(x,y).getOccupant()+" is our "+b.getFloorPlan(i).roomAt(x,y).getPosition()+" and can be located on the "+b.getFloorPlan(i).getName());
                                        alert.showAndWait();
                                    }
                                }
                            }
                        }
                    }
                }else{
                    alert.setContentText("That name does not match a name in our records,please try again");
                    alert.showAndWait();
                }

            }
        });

    }
    public String toString(Room r){
        return r.getNumber()+"-"+r.getOccupant()+"("+r.getPosition()+")";
    }
    public String toString2(Room r){
        return r.getOccupant()+" is our "+r.getPosition()+" and can be located in room "+r.getNumber();
    }
}

