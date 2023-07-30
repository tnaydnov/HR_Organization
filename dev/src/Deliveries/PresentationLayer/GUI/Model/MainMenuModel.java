package Deliveries.PresentationLayer.GUI.Model;


import Deliveries.BusinessLayer.*;
import Deliveries.BusinessLayer.Generators.SiteGenerator;
import Deliveries.PresentationLayer.GUI.View.AddDeliveryFrame;
import Deliveries.PresentationLayer.GUI.View.ExecuteDeliveriesFrame;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MainMenuModel extends AbstractModel {
    private final DeliveryManagerImpl deliveryManager;
    private final DeliveryFormsController deliveryFormsController;
    private final List<Site> sitesList;
    public MainMenuModel() {
        deliveryManager = DeliveryManagerImpl.getInstance(); //removed the use of service class
        deliveryFormsController = deliveryManager.getDeliveryFormsController();
        sitesList = new SiteGenerator().getSitesList();
    }
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getActionCommand().equals("Add a delivery stop")) {
            AddDeliveryStopClicked();
        }
        else if (e.getActionCommand().equals("Remove a delivery stop")) {
            RemoveDeliveryStopClicked();
        }
        else if (e.getActionCommand().equals("Execute a delivery")) {
            ExecuteDeliveriesClicked();
        }
        else if (e.getActionCommand().equals("Exit")) {
            System.exit(0);
        }
    }

    private void AddDeliveryStopClicked() {
        // TODO: Implement this
        //relatedFrame.displayError("Not implemented yet :(");
        relatedFrame.dispose();

        new AddDeliveryFrame(sitesList);

    }

    private void RemoveDeliveryStopClicked() {
        List<DeliveryStop> stops = new ArrayList<>();
        for (DeliveryStop stop : deliveryManager.getPendingDeliveryStops()) {
            stops.add(stop);
        }
        // sort by origin name, then by destination name alphabetically
        stops.sort(Comparator.comparing((DeliveryStop stop) -> stop.getOrigin().getName()).
                thenComparing(stop -> stop.getDestination().getName()));
        JComboBox<DeliveryStop> stopsComboBox = new JComboBox<>(stops.toArray(new DeliveryStop[0]));
        stopsComboBox.setRenderer(new DefaultListCellRenderer() {
            // This block makes the combobox display the origin and destination of the delivery stop
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                if (value instanceof DeliveryStop stop) {
                    String text = stop.getOrigin() + " to " + stop.getDestination();
                    return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
                } else {
                    return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                }
            }
        });
        int result = JOptionPane.showConfirmDialog(relatedFrame, stopsComboBox,
                "Select a delivery stop to remove", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            DeliveryStop deliveryStop = (DeliveryStop) stopsComboBox.getSelectedItem();
            deliveryManager.removeDeliveryStop(Objects.requireNonNull(deliveryStop).getShipmentInstanceID());
            relatedFrame.displayInfo("Delivery stop removed successfully!");
        }
        else relatedFrame.displayInfo("Removal cancelled, no delivery stop was removed");
        // A little ugly to manage it all in 1 function, but it has very little logic so it's fine
    }

    public void ExecuteDeliveriesClicked(){
        Set<DeliveryForm> pendingDeliveryForms = deliveryFormsController.getPendingDeliveryForms();

        if (pendingDeliveryForms.isEmpty()) {
            deliveryManager.createDeliveryGroup();
            pendingDeliveryForms = deliveryFormsController.getPendingDeliveryForms();
            // Breaks separation of concerns, should be fixed using a service layer
        }
        relatedFrame.dispose();
        new ExecuteDeliveriesFrame(pendingDeliveryForms);
    }
}
