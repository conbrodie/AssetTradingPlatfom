package client.gui.forms;

import client.ClientLogic;
import common.models.AssetModel;
import common.models.TradeHistoryModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Chart extends JDialog {

    private final String ASSET_COMBOBOX_USER_HINT = "  Dropdown list and select...  ";

    private JPanel rootPanel;
    private JButton drawChartButton;
    private JLabel nameLabel;
    private JComboBox<Asset> assetCombobox;
    private JPanel graphPanel;
    private int asset_id;
    private String asset_name;
    private ArrayList<AssetModel> assets; // list for assetCombobox
    private ArrayList<TradeHistoryModel> assetHistory; // list for graphing

    public Chart(JFrame parent, String title, boolean modal, ClientLogic objClientLogic, ArrayList<AssetModel> assets) {
        super(parent, title, modal);

        this.assets = assets; // assign assets parameter to class variable

        rootPanel = new JPanel(new BorderLayout());
        this.setContentPane(rootPanel);

        nameLabel = new JLabel("Select an asset to graph  ");
        JPanel panel = new JPanel(new FlowLayout());

        AssetComboBoxModel model = new AssetComboBoxModel(loadAssets());
        assetCombobox = new JComboBox<Asset>();
        assetCombobox.setModel(model);
        assetCombobox.setPreferredSize(new Dimension(350, 26));

        drawChartButton = new JButton("Draw Chart");
        panel.add(nameLabel);
        panel.add(assetCombobox);
        panel.add(drawChartButton);
        rootPanel.add(panel, BorderLayout.NORTH);

        graphPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(graphPanel); // holds graph
        rootPanel.add(scrollPane, BorderLayout.CENTER);

        graphPanel.setPreferredSize(new Dimension(700, 460));
        this.pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // Note: This is a model Dialog - so call setVisible(true) from parent form that created it.

        assetCombobox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox assetCombobox = (JComboBox) e.getSource();
                Asset asset = (Asset) assetCombobox.getSelectedItem();
                asset_id = asset.getId();
                asset_name = asset.getAsset_name();
                if (asset_id > 0) {
                    assetHistory = objClientLogic.getTradeHistory(asset_name); // get selective TradeHistory data
                    if (assetHistory == null) {
                        graphPanel.removeAll();
                        graphPanel.repaint();
                        JOptionPane.showMessageDialog(getParent(), "No trades available for asset '" + asset_name + "'",
                                "Graphing Message", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                else { // clear graph panel if user selects 'Dropdown list and select...' option
                    graphPanel.removeAll();
                    graphPanel.repaint();
                }
            }
        });

        drawChartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ( ((Asset)assetCombobox.getSelectedItem() ).getId() < 0) { return; } // can't 'Choose an asset to graph'

                JFreeChart timeSeriesChart = null;
                try {
                    //String title = assetHistory.get(0).getAsset_name();
                    // Alternate chart variation
//                    timeSeriesChart = ChartFactory.createTimeSeriesChart("Trade History - CPU Hours",
//                            "Date", "Price", xyDataset,
//                            true, false, false);

                    // This dataset must not have two points with same time! i.e. milli-sec the same
                    XYDataset xyDataset = createTradedSeriesDataset();
                    if (xyDataset == null) { return; }

                    timeSeriesChart = ChartFactory.createScatterPlot("Trading History - " + asset_name,
                            "Date", "Price $", xyDataset,
                            PlotOrientation.VERTICAL, true, false, false);

                    XYPlot plot = timeSeriesChart.getXYPlot();
                    XYItemRenderer renderer = plot.getRenderer();

                    plot.setDomainAxis(new DateAxis()); // change x axis to DateAxis

                    DateAxis domain = (DateAxis) plot.getDomainAxis();
                    domain.setDateFormatOverride(new SimpleDateFormat("dd/MM/yyyy"));
                    domain.setLabel("Date Processed");
                    domain.setLabelFont(new  Font("SansSerif", Font.PLAIN, 12));

                    // Change colour of chart background and gridlines
                    timeSeriesChart.getPlot().setBackgroundPaint(new Color(245, 245, 245));
                    renderer.setSeriesPaint(0, new Color(255, 102, 102));
                    //renderer.setSeriesPaint( 1 , new Color(102,178,255) );
                    //renderer.setSeriesPaint( 1 , new Color(102,255,102) );
                    renderer.setSeriesPaint(1, new Color(102, 102, 255));

                    // Change colour of line or marker
                    plot.setDomainGridlinePaint(new Color(128, 128, 128));
                    plot.setDomainGridlinesVisible(true);
                    plot.setRangeGridlinePaint(new Color(128, 128, 128));
                    plot.setRangeGridlinesVisible(true);

                    // Change line thickness ( Line chart )
                    //renderer.setSeriesStroke( 0 , new BasicStroke( 3.0f ) );
                    //renderer.setSeriesStroke( 1 , new BasicStroke( 3.0f ) );

                    // Change chart marker size ( Scatter chart )
                    double size = 10.0;
                    double delta = size / 2.0;
                    Shape shape1 = new Rectangle2D.Double(-delta, -delta, size, size);
                    Shape shape2 = new Ellipse2D.Double(-delta, -delta, size, size);
                    renderer.setSeriesShape(0, shape1);
                    renderer.setSeriesShape(1, shape2);

                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }

                ChartPanel chartPanel = new ChartPanel(timeSeriesChart);
                chartPanel.setPreferredSize(new Dimension(700, 460));
                graphPanel.removeAll();
                graphPanel.add(chartPanel, BorderLayout.CENTER);
                graphPanel.revalidate();
            }
        });
    }

    private XYDataset createTradedSeriesDataset() throws ParseException {
        // For single Asset only
        // {null, null, SELL, Computer Cluster Division, jones.r, CPU Hours, 25, 10, 2021-04-19 16:35:32.0, 2021-04-19 11:38:58.0}

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        TimeSeries buy = new TimeSeries("Buy");
        TimeSeries sell = new TimeSeries("Sell");
        TimeSeriesCollection dataset = null;
        try {
            int i = 1;
            for (TradeHistoryModel thm : assetHistory) {
                if (thm.getAsset_name().equals(this.asset_name)) {
                    Date SellDate = new Date();
                    Date BuyDate = new Date();
                    if (thm.getTrade_type().equals("BUY")) {
                        // Add a second to BUY separate BUY from SELL
//                        Timestamp original = thm.getDate_processed();
//                        Timestamp later = new Timestamp(original.getTime() + (1 * 1000L));
//                        later.setNanos(original.getNanos());
                        Timestamp ts = thm.getDate_processed();
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(ts.getTime());
                        cal.add(Calendar.SECOND, i++);
                        Timestamp later = new Timestamp(cal.getTime().getTime());
                        buy.add(new FixedMillisecond(later), thm.getPrice());
                        System.out.println("buy " + later);
                        //buy.add(new FixedMillisecond(thm.getDate_processed()), thm.getPrice());
                    } else if (thm.getTrade_type().equals("SELL")) {
                        Timestamp original = thm.getDate_processed();
                        Timestamp later = new Timestamp(original.getTime() - (i++ * 1000L));
                        later.setNanos(original.getNanos());
                        sell.add(new FixedMillisecond(later), thm.getPrice());
                        System.out.println("sell " + later);
                        //sell.add(new FixedMillisecond(thm.getDate_processed()), thm.getPrice());
                    }
//                if (thm.getTrade_type().equals("BUY")) {
//                    buy.add(new FixedMillisecond(formatter.parse(thm.getDate_processed())), Integer.parseInt(thm.getPrice()));
//                } else if (thm.getTrade_type().equals("SELL")) {
//                    sell.add(new FixedMillisecond(formatter.parse(thm.getDate_processed())), Integer.parseInt(thm.getPrice()));
//                }
                }
            }
            dataset = new TimeSeriesCollection();
            dataset.addSeries(buy);
            dataset.addSeries(sell);


        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "An error occurred in generating the chart data!\nContact your system administrator!",
                    "Trade History Chart", JOptionPane.INFORMATION_MESSAGE);
        }
        return dataset;
    }

    // Manage ComboBox - assertCombobox
    class AssetComboBoxModel extends DefaultComboBoxModel<Asset> {
        public AssetComboBoxModel(Asset[] assets) {
            super(assets);
        }
        @Override
        public Asset getSelectedItem() {
            Asset selectedAsset = (Asset) super.getSelectedItem();
            // Action can be performed on asset
            return selectedAsset;
        }
    }

    private class Asset {
        private int id;
        private String asset_name;

        public Asset(int id, String asset_name) { this.id = id; this.asset_name = asset_name; }
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getAsset_name() { return asset_name; }
        public void setAsset_name(String asset_name) { this.asset_name = asset_name; }

        @Override
        public String toString() { return asset_name; }
    }

    private Asset[] loadAssets() {
        Asset[] assets = new Asset[this.assets.size()+1];
        int i = 0;
        assets[i++] = new Asset(-1,ASSET_COMBOBOX_USER_HINT); // add 'Dropdown list and select...' option to asset list
        for (AssetModel asset : this.assets) {
            assets[i++] = new Asset(asset.getAsset_id(), asset.getAsset_name());
        }
        return assets;
    }
}