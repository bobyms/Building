import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class FloorBuilderApp extends Application  {
    private FloorBuilderView   view;
    private Building           model;
    private int                currentFloor;    // Index of the floor being displayed
    private int                currentColor;// Index of the current room color

    public void start(Stage primaryStage) {
        model = Building.example();
        currentFloor = 0;
        currentColor = 0;

        FloorPlan[] floors = model.getFloorPlans();

        VBox aPane = new VBox();
        view = new FloorBuilderView(model);
        view.setPrefWidth(Integer.MAX_VALUE);
        view.setPrefHeight(Integer.MAX_VALUE);

        Menu floormenu = new Menu("Select Floor");
        MenuBar menubar = new MenuBar();
        int i = 0;
        MenuItem floor1 = new MenuItem(floors[i].getName());
        MenuItem floor2 = new MenuItem(floors[i+1].getName());
        MenuItem floor3 = new MenuItem(floors[i+2].getName());
        MenuItem floor4 = new MenuItem(floors[i+3].getName());
        MenuItem floor5 = new MenuItem(floors[i+4].getName());
        floormenu.getItems().addAll(floor4, floor3, floor2, floor1, new SeparatorMenuItem(), floor5);
        menubar.getMenus().add(floormenu);
        aPane.getChildren().add(menubar);
        aPane.getChildren().add(view);
        primaryStage.setTitle("Floor Plan Builder");
        primaryStage.setScene(new Scene(aPane, 370,340));
        primaryStage.show();

        // Plug in the floor panel event handlers:
        for (int r=0; r<model.getFloorPlan(0).size(); r++) {
            for (int c=0; c<model.getFloorPlan(0).size(); c++) {
                view.getFloorTileButton(r, c).setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        handleTileSelection(event);
                    }});
            }
        }

        // Plug in the radioButton event handlers
        view.getEditWallsButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                view.update(currentFloor, currentColor);
            }});
        view.getSelectExitsButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                view.update(currentFloor, currentColor);
            }});
        view.getEditRoomsButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                view.update(currentFloor, currentColor);
            }});
        view.getDefineRoomsButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                view.update(currentFloor, currentColor);
            }});

        // Plug in the office color button
        view.getRoomColorButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                currentColor = (currentColor + 1)%view.ROOM_COLORS.length;
                view.update(currentFloor, currentColor);
            }});
        // Shows building dialog
        view.getBuildingOverviewButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Dialog dialog = new BuildingDialog(primaryStage, model);
                dialog.showAndWait();
            }
        });

        floor1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                currentFloor = 0;
                view.update(currentFloor, currentColor);
            }
        });
        view.update(currentFloor, currentColor);

        floor2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                currentFloor = 1;
                view.update(currentFloor, currentColor);
            }
        });
        floor3.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                currentFloor = 2;
                view.update(currentFloor, currentColor);
            }
        });
        floor4.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                currentFloor = 3;
                view.update(currentFloor, currentColor);
            }
        });
        floor5.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                currentFloor = 4;
                view.update(currentFloor, currentColor);
            }
        });
        view.update(currentFloor, currentColor);
    }

    // Handle a Floor Tile Selection
    private void handleTileSelection(ActionEvent e) {
        //Makes not room alert
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Selection");
        alert.setHeaderText(null);
        alert.setContentText("You must select a tile that belongs to a room");

        //Makes room info dialog box
        Dialog RoomInfoDialog = new Dialog();
        RoomInfoDialog.setTitle("Room Details");
        RoomInfoDialog.setHeaderText(null);
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(10,10,10,10));
        grid.add(new Label("Occupant:"), 0, 0);
        grid.add(new Label("Position:"), 0, 1);
        grid.add(new Label("Number:"), 0, 2);
        grid.add(new Label("Floor:"), 0, 3);
        grid.add(new Label("Size:"), 0, 4);
        TextField oc = new TextField();
        oc.setPromptText("Person who occupies this room");
        grid.add(oc,1,0);
        TextField po = new TextField();
        po.setPromptText("Job position/title of this person");
        grid.add(po,1,1);
        TextField nu = new TextField();
        nu.setPromptText("The room number");
        grid.add(nu,1,2);
        TextField fl = new TextField();
        grid.add(fl,1,3);
        TextField si = new TextField();
        grid.add(si,1,4);
        TextField col = new TextField();
        grid.add(col,2,2);
        si.setEditable(false);
        fl.setEditable(false);
        col.setStyle("-fx-base: " + view.ROOM_COLORS[currentColor]);
        col.setFocusTraversable(false);
        col.setEditable(false);
        RoomInfoDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK,ButtonType.CANCEL);
        RoomInfoDialog.getDialogPane().setContent(grid);

        // Determine which row and column was selected
        int r=0, c=0;
        OUTER:
        for (r=0; r<model.getFloorPlan(0).size(); r++) {
            for (c=0; c<model.getFloorPlan(0).size(); c++) {
                if (e.getSource() == view.getFloorTileButton(r, c))
                    break OUTER;
            }
        }

        // Check if we are in edit wall mode, then toggle the wall
        if (view.getEditWallsButton().isSelected()) {
            model.getFloorPlan(currentFloor).setWallAt(r, c, !model.getFloorPlan(currentFloor).wallAt(r, c));
            // Remove this tile from the room if it is on one, because it is now a wall
            Room room = model.getFloorPlan(currentFloor).roomAt(r, c);
            if (room != null)
                room.removeTile(r, c);
        }

        // Otherwise check if we are in edit exits mode
        else if (view.getSelectExitsButton().isSelected()) {
            if (model.exitAt(r, c) != null)
                model.removeExit(r, c);
            else {
                model.addExit(r, c);
                // Remove this tile from the room if it is on one, because it is now an exit
                Room off = model.getFloorPlan(currentFloor).roomAt(r, c);
                if (off != null)
                    off.removeTile(r, c);
            }
        }

        // Otherwise check if we are selecting a room tile
        else if (view.getEditRoomsButton().isSelected()) {
            if (!model.getFloorPlan(currentFloor).wallAt(r, c) && !model.hasExitAt(r, c)) {
                Room room = model.getFloorPlan(currentFloor).roomAt(r, c);
                if (room != null) {
                    room.removeTile(r, c);
                    if (room.getNumberOfTiles() == 0)
                        model.getFloorPlan(currentFloor).removeRoom(room);
                }
                else {
                    room = model.getFloorPlan(currentFloor).roomWithColor(currentColor);
                    if (room == null) {
                        room = model.getFloorPlan(currentFloor).addRoomAt(r, c);
                        room.setColorIndex(currentColor);
                    }
                    else {
                        room.addTile(r, c);
                    }
                }
            }
        }
        //Checks if select room button is pressed
        else if(view.getDefineRoomsButton().isSelected()){
            if(model.getFloorPlan(currentFloor).roomAt(r,c)==null) {
                alert.showAndWait();
            }
            else{
                fl.setText(model.getFloorPlan(currentFloor).getName());
                si.setText(model.getFloorPlan(currentFloor).roomAt(r,c).getNumberOfTiles()+"Sq. Ft");
                Optional info = RoomInfoDialog.showAndWait();
                if(info.isPresent()){
                    model.getFloorPlan(currentFloor).roomAt(r,c).setNumber(nu.getText());
                    model.getFloorPlan(currentFloor).roomAt(r,c).setOccupant(oc.getText());
                    model.getFloorPlan(currentFloor).roomAt(r,c).setPosition(po.getText());
                }
            }
        }
        // Otherwise do nothing
        else {
        }

        view.update(currentFloor, currentColor);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

