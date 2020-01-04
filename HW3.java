/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HW3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class HW3 extends JPanel {
    
    // Class Properties
    
    // Global settings:   
    
    static final boolean fullSizeWindow = false;
    static final boolean guiDebug = false;
    static final int GUI_WIDTH = 800; // 1600
    static final int GUI_HEIGHT = 800; // 1200
    
    // 
    static final int numXGrid = GUI_WIDTH / 10; // 160
    static final int numYGrid = GUI_HEIGHT / 10; // 120

    // These define the vertical levels of placement in GUI:        
    static final int yLevel0 = 0;
    static final int yLevel1 = 5;
    static final int yLevel2 = 85;
    static final int yLevel3 = 90;
    static final int yLevel4 = 95;    
    
    static final int xLevel0 = 0;
    static final int xLevel1 = 30;
    static final int xLevel2 = 60;
    static final int xLevel3 = 90;
    
    static final int xLevel01 = 10;
    static final int xLevel02 = 20;    
    static final int xLevel03 = 30;    
    static final int xLevel04 = 40;
    static final int xLevel05 = 50;
    
    
    
    // Initialize Swing components:
   
    JDialog revDiag = new JDialog();
    JButton searchButton = new JButton();
    
    
    // 1. JComboBox
    JComboBox jcb_selectChoice;
    JComboBox jcb_closeHour;
    JComboBox jcb_openHour;
    JComboBox jcb_dow;
    
    // 2. JComboBox
    JTable table;
    ResultSet rs = null;
    
    // 3. JScrollPane
    JScrollPane jsp_maincat;
    JScrollPane jsp_subcat;
    JScrollPane jsp_att;
    JScrollPane jsp_table;
    JScrollPane jsp_dow;
    JScrollPane jsp_openHour;
    JScrollPane jsp_closeHour;
    JScrollPane jsp_selectChoice;  
    JScrollPane diagP;    
    
    // Text Labels:    
    JScrollPane jsp_label_maincat; 
    JScrollPane jsp_label_subcat;
    JScrollPane jsp_label_att;
    JScrollPane jsp_label_table;
    JScrollPane jsp_label_dow;
    JScrollPane jsp_label_openH;
    JScrollPane jsp_label_closeH;    
    JScrollPane jsp_label_filter;
    
    // 4. JList
    JList jl_maincat = new JList();
    JList jl_subcat = new JList();
    JList jl_att = new JList();  
    
    // 5. JLabel
    JLabel label_Title = new JLabel();
    JLabel label_maincat = new JLabel();
    JLabel label_subcat = new JLabel();
    JLabel label_att = new JLabel();
    JLabel label_table = new JLabel();
    JLabel label_dow = new JLabel();
    JLabel label_openH = new JLabel();
    JLabel label_closeH = new JLabel();
    JLabel label_filter = new JLabel();
    
    // Initialize SQL variables    
    Connection dbCon = null;
    PreparedStatement stmt = null;
    
    // Initialize List    
    List dl_maincat = new ArrayList();
    List dl_subcat = new ArrayList();
    List dl_att = new ArrayList();
    List jL_dow = new ArrayList();
    List jL_openHour = new ArrayList();    
    List jL_selectChoice = new ArrayList();
    
    public static void main(String[] args) { 
        String laf = UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(laf);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HW3.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(HW3.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(HW3.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(HW3.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            JFrame frame = new JFrame("YELP DATA SEARCH TOOL");            
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);            
            frame.getContentPane().setBackground(new Color(0 , 0 , 0));
            frame.setSize(new Dimension(GUI_WIDTH, GUI_HEIGHT));
            frame.setContentPane(new HW3());
            frame.pack();            
            if (!fullSizeWindow){
                frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            }
            frame.setVisible(true);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HW3.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(HW3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public HW3() throws ClassNotFoundException, SQLException {
        
        diagP = new JScrollPane(new JLabel("Display"));        
        revDiag.add(diagP);
        if (!guiDebug){
            Connection_DB con = new Connection_DB();
            dbCon = con.get_DBConn();
            // Get Main Category list from dB            
            String sql = "Select * from hw_m_category";
            stmt = dbCon.prepareStatement(sql);
            rs = stmt.executeQuery(); 
            // build List of Categories
            while (rs.next()) {
                String catg = rs.getString(1);
                dl_maincat.add(catg);
            }
        } else {
            // Debug Mode : build dummy List of Categories
            int cnt = 0;
            while (cnt < 10) {
                String catg = "Item" + Integer.toString(cnt);
                dl_maincat.add(catg);
                cnt++;
            }
        }
         
        // Use gridbaglayout for object placement
        setLayout(new GridBagLayout());
        
        // Categories
        jl_maincat = new JList(dl_maincat.toArray()); //datlis        
        jl_maincat.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jl_maincat.addListSelectionListener(new GetSubCategoryData());
        jsp_maincat = new JScrollPane(jl_maincat); 
        jsp_maincat.setVerifyInputWhenFocusTarget(guiDebug);        
        //jsp_maincat.setPreferredSize(new Dimension(110*3, 120*6));
              
        
        // Sub- Categories
        dl_subcat = new ArrayList();        
        jl_subcat = new JList(dl_subcat.toArray());
        jl_subcat.addListSelectionListener(new GetAttData());        
        jl_subcat.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jsp_subcat = new JScrollPane(jl_subcat);
        //jsp_subcat.setPreferredSize(new Dimension(110*3, 120*6));
               
        // Attribute List
        jl_att = new JList(dl_att.toArray());
        jl_att.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jsp_att = new JScrollPane(jl_att);
        //jsp_att.setPreferredSize(new Dimension(110*3, 120*6));
        
        // Business and ratings box
        jsp_table = new JScrollPane(table);
        //jsp_table.setPreferredSize(new Dimension(110*6, 120*10));
        jsp_table.addMouseListener(new showReviews());
                
        // Generate Day of week array
        jL_dow = new ArrayList();
        jL_dow.add("---- Select ----");
        /*
        jL_dow.add("Sunday");
        jL_dow.add("Monday");
        jL_dow.add("Tuesday");
        jL_dow.add("Wednesday");
        jL_dow.add("Thursday");
        jL_dow.add("Friday");
        jL_dow.add("Saturday");
        */
        jcb_dow = new JComboBox(jL_dow.toArray());
        jcb_dow.setLightWeightPopupEnabled(false);
        jsp_dow = new JScrollPane(jcb_dow);
        //jsp_dow.setMinimumSize(new Dimension(200,20));
        //jsp_dow.setSize(new Dimension(100,10));
        jsp_dow.setPreferredSize(jsp_dow.getPreferredSize());
        
        // Generate Time of Day array
        jL_openHour = new ArrayList();
        jL_openHour.add("Select");
        
        jcb_openHour = new JComboBox(jL_openHour.toArray());
        jsp_openHour = new JScrollPane(jcb_openHour);
        
        // Use same array for Closing Time
        jcb_closeHour = new JComboBox(jL_openHour.toArray());
        jsp_closeHour = new JScrollPane(jcb_closeHour);
       
        // Settup Attribute filter style
        jL_selectChoice = new ArrayList();
        jL_selectChoice.add("AND");
        jL_selectChoice.add("OR");
        jcb_selectChoice = new JComboBox(jL_selectChoice.toArray());
        jsp_selectChoice = new JScrollPane(jcb_selectChoice);
                
        // Search Button
        searchButton.setText("Search");
        searchButton.addActionListener(new showSearchResults());        
        searchButton.setBackground(Color.blue);
        searchButton.setForeground(Color.WHITE);
        //searchButton.setSize(new Dimension(100,40));
        
        /*
        jl_maincat = new JList(dl_maincat.toArray()); //datlis        
        jl_maincat.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jl_maincat.addListSelectionListener(new GetSubCategoryData());
        jsp_maincat = new JScrollPane(jl_maincat);        
        */
        //jsp_maincat.setPreferredSize(new Dimension(110*3, 120*6));  
        
        // setup text labels :
                              
        label_maincat = new JLabel("Main Category");
        jsp_label_maincat = new JScrollPane(label_maincat);
        //jsp_label_maincat.setPreferredSize(new Dimension(30, 20));
        jsp_label_maincat.setBorder(null);
        
        label_subcat = new JLabel("Sub Category");
        jsp_label_subcat = new JScrollPane(label_subcat);
        //jsp_label_subcat.setPreferredSize(new Dimension(30, 20));
        jsp_label_subcat.setBorder(null);
        
        label_att = new JLabel("Attributes");
        jsp_label_att = new JScrollPane(label_att);
        //jsp_label_att.setPreferredSize(new Dimension(30, 20));
        jsp_label_att.setBorder(null);
        
        label_table = new JLabel("Search Results");
        jsp_label_table = new JScrollPane(label_table);
        //jsp_label_table.setPreferredSize(new Dimension(30, 20));        
        jsp_label_table.setBorder(null);
        
        label_dow = new JLabel("Select Day");
        jsp_label_dow = new JScrollPane(label_dow);
        //jsp_label_dow.setPreferredSize(new Dimension(30, 20));
        jsp_label_dow.setBorder(null);
        
        label_openH = new JLabel("Pick Start Time");
        jsp_label_openH = new JScrollPane(label_openH);
        //jsp_label_openH.setPreferredSize(new Dimension(30, 20));
        jsp_label_openH.setBorder(null);
        
        label_closeH = new JLabel("Pick End Time");
        jsp_label_closeH = new JScrollPane(label_closeH);
        //jsp_label_closeH.setPreferredSize(new Dimension(30, 20));
        jsp_label_closeH.setBorder(null);
        
        label_filter = new JLabel("Attribute AND/OR");
        jsp_label_filter = new JScrollPane(label_filter);
        //jsp_label_filter.setPreferredSize(new Dimension(30, 20));
        jsp_label_filter.setBorder(null);
        
        // Setup Gridbaglayout positions for objects:        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        //gbc.insets = new Insets(5,5,5,5);
        
        // Main Category : JList
        
        gbc.gridx = xLevel0;      gbc.gridy = yLevel0;
        gbc.gridwidth = xLevel1 - xLevel0;  gbc.gridheight = yLevel1 - yLevel0;
        add(jsp_label_maincat,gbc);
        
        gbc.gridx = xLevel0;      gbc.gridy = yLevel1;
        gbc.gridwidth = xLevel1 - xLevel0;  gbc.gridheight = yLevel2 - yLevel1;        
        add(jsp_maincat,gbc);
        
        // Sub Category : JList
        gbc.gridx = xLevel1;      gbc.gridy = yLevel0;
        gbc.gridwidth = xLevel2 - xLevel1;  gbc.gridheight = yLevel1 - yLevel0;
        add(jsp_label_subcat,gbc);
        
        gbc.gridx = 30;      gbc.gridy = yLevel1;
        gbc.gridwidth = 30;  gbc.gridheight = yLevel2 - yLevel1;
        add(jsp_subcat, gbc);
        
        // Business Attributes : JList
        gbc.gridx = 60;      gbc.gridy = yLevel0;
        gbc.gridwidth = 30;  gbc.gridheight = yLevel1 - yLevel0;
        add(jsp_label_att,gbc);
        
        gbc.gridx = 60;      gbc.gridy = yLevel1;
        gbc.gridwidth = 30;  gbc.gridheight = yLevel2 - yLevel1;
        add(jsp_att, gbc);
        
        // Table
        gbc.gridx = 90;      gbc.gridy = yLevel0;
        gbc.gridwidth = 70;  gbc.gridheight = yLevel1 - yLevel0;
        add(jsp_label_table,gbc);
        
        gbc.gridx = 90;      gbc.gridy = yLevel1;
        gbc.gridwidth = 70;  gbc.gridheight = 110;
        add(jsp_table, gbc); 
              
        // Filter : Day of the week
        gbc.gridx = 0;      gbc.gridy = yLevel2;
        gbc.gridwidth = 30;  gbc.gridheight = yLevel3 - yLevel2;
        add(jsp_label_dow,gbc);
        
        gbc.gridx = 0;      gbc.gridy = yLevel3;
        gbc.gridwidth = 30;  gbc.gridheight = yLevel4 - yLevel3;        
        add(jsp_dow, gbc);
        
        // Filter : Open Hours
        gbc.gridx = 30;      gbc.gridy = yLevel2;
        gbc.gridwidth = 10;  gbc.gridheight = yLevel3 - yLevel2;
        add(jsp_label_openH,gbc);
        
        gbc.gridx = 30;      gbc.gridy = yLevel3;
        gbc.gridwidth = 10;  gbc.gridheight = yLevel4 - yLevel3;
        add(jsp_openHour, gbc);
        
        // Filter : Closing Hour
        gbc.gridx = 40;      gbc.gridy = yLevel2;
        gbc.gridwidth = 10;  gbc.gridheight = yLevel3 - yLevel2;
        add(jsp_label_closeH,gbc);
        
        gbc.gridx = 40;      gbc.gridy = yLevel3;
        gbc.gridwidth = 10;  gbc.gridheight = yLevel4 - yLevel3;
        add(jsp_closeHour, gbc);
        
        // Filter : Selector
        gbc.gridx = 50;      gbc.gridy = yLevel2;
        gbc.gridwidth = 10;  gbc.gridheight = yLevel3 - yLevel2;
        add(jsp_label_filter,gbc);
        
        gbc.gridx = 50;      gbc.gridy = yLevel3;
        gbc.gridwidth = 10;  gbc.gridheight = yLevel4 - yLevel3;
        add(jsp_selectChoice, gbc);
        
        // Search Button
        gbc.gridx = 60;      gbc.gridy = yLevel2;
        gbc.gridwidth = 20;  gbc.gridheight = yLevel4 - yLevel2;        
        add(searchButton, gbc);
    }

    public class showSearchResults implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                rs = null;
                // get variables:
                String andor = jcb_selectChoice.getSelectedItem().toString();
                int[] mainCatArr = jl_maincat.getSelectedIndices();
                int[] subCatArr = jl_subcat.getSelectedIndices();
                int[] attArr = jl_att.getSelectedIndices();
                int dowIndex = jcb_dow.getSelectedIndex();
                int oHIndex = jcb_openHour.getSelectedIndex();
                int cHIndex = jcb_closeHour.getSelectedIndex();
                
                // get bid query:
                String q1,q2,q3,q4,q5,q6,q7,q8,q9;
                
                String catName = null;
                String subCatName = null;
                String attName = null;
                String startH = null;
                String stopH = null;
                String dayOfWeek = null;
                
                boolean dow_null = true;
                boolean startH_null = true;
                boolean stopH_null = true;
                boolean mainCat_isempty = true;
                boolean subCat_isempty = true;
                boolean att_isempty = true;
                
                if (mainCatArr.length > 0){
                    // this is required;
                    mainCat_isempty = false;                    
                }
                if (subCatArr.length > 0){
                    // this is required;
                    subCat_isempty = false;
                }
                if (attArr.length > 0){
                    // this is required;
                    att_isempty = false;
                }                
                if (!(jcb_dow.getSelectedItem().toString().contains("Select"))){
                    dayOfWeek = jcb_dow.getSelectedItem().toString();
                    dow_null = false;
                } 
                if (!(jcb_openHour.getSelectedItem().toString().contains("Select"))){
                    startH = jcb_openHour.getSelectedItem().toString();
                    startH_null = false;
                } 
                if (!(jcb_closeHour.getSelectedItem().toString().contains("Select"))){
                    stopH = jcb_closeHour.getSelectedItem().toString();
                    stopH_null = false;
                } 
                
                
                
                
                q1 = "select distinct mcat.bid from hw_business_main_category mcat,hw_b_hours bhrs,hw_business_attributes att where mcat.cat_name='~~~main!!!'";
                q2 = " and mcat.sub_cat_name='~~~sub!!!'";
                q3 = " and att.attr_name='~~~att!!!' and mcat.bid=att.bid";
                
                q4 = " and mcat.bid in (select bhrs.bid from hw_b_hours bhrs where bhrs.bid=mcat.bid";
                q5 = " and bhrs.from_hr='" + startH + "'";
                q6 = " and bhrs.to_hr='" + stopH + "'";
                q7 = " and bhrs.day_of_week='" + dayOfWeek + "'";
                q8 = ")";
                
                if (att_isempty){
                    q1 = "select distinct mcat.bid from hw_business_main_category mcat,hw_b_hours bhrs where mcat.cat_name='~~~main!!!'";
                }
                
                int numStatements = 1;
                String genQuery = "";
                if (!subCat_isempty){
                    if (!att_isempty){
                        numStatements = mainCatArr.length * subCatArr.length * attArr.length;
                        genQuery = q1 + q2 + q3;
                    }else {
                        numStatements = mainCatArr.length * subCatArr.length;
                        genQuery = q1 + q2;
                    }
                } else {
                    numStatements = mainCatArr.length;
                    genQuery = q1;
                }
                
                if (!dow_null | !startH_null | !stopH_null){
                    genQuery = genQuery + q4;
                    if (!dow_null){
                        //genQuery = genQuery + q5;
                        genQuery = genQuery + q7;
                    }
                    if (!startH_null){
                        //genQuery = genQuery + q6;
                        genQuery = genQuery + q5;
                    }
                    if (!stopH_null){
                        //genQuery = genQuery + q7;
                        genQuery = genQuery + q6;
                    }
                    genQuery = genQuery + q8;
                }
                      
                String t = "";
                String query = "";
                if (!att_isempty){
                    // all three parameters
                    for (int kk = 0; kk < mainCatArr.length; kk++) {
                        catName = String.valueOf(jl_maincat.getModel().getElementAt(mainCatArr[kk]));                        
                        if (!subCat_isempty) {
                            for (int jj = 0; jj < subCatArr.length; jj++) {
                                subCatName = String.valueOf(jl_subcat.getModel().getElementAt(subCatArr[jj]));                                
                                if (!att_isempty) {
                                    for (int ii = 0; ii < attArr.length; ii++) {
                                        attName = String.valueOf(jl_att.getModel().getElementAt(attArr[ii]));
                                        t = genQuery.replaceAll("~~~att!!!", attName);
                                        t = t.replaceAll("~~~sub!!!", subCatName);
                                        t = t.replaceAll("~~~main!!!", catName);
                                        if ((kk == mainCatArr.length-1) & (jj == subCatArr.length-1) & (ii == attArr.length-1)){
                                            query = query + t; 
                                        } else {
                                            if (andor.equals("AND")) {
                                                query = query + t + " intersect ";
                                            } else {
                                                query = query + t + " union ";
                                            }                                                                                       
                                        }
                                    }
                                }
                            }
                        }
                    }                    
                } else {
                    if (!subCat_isempty){
                        // two parameters
                        for (int kk = 0; kk < mainCatArr.length; kk++) {
                            catName = String.valueOf(jl_maincat.getModel().getElementAt(mainCatArr[kk]));
                            if (!subCat_isempty) {
                                for (int jj = 0; jj < subCatArr.length; jj++) {
                                    subCatName = String.valueOf(jl_subcat.getModel().getElementAt(subCatArr[jj]));                                    
                                    t = genQuery.replaceAll("~~~sub!!!", subCatName);
                                    t = t.replaceAll("~~~main!!!", catName);
                                    if ((kk == mainCatArr.length - 1) & (jj == subCatArr.length - 1)) {
                                        query = query + t;
                                    } else {
                                        if (andor.equals("AND")) {
                                            query = query + t + " intersect ";
                                        } else {
                                            query = query + t + " union ";
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        // one parameters
                        for (int kk = 0; kk < mainCatArr.length; kk++) {
                            catName = String.valueOf(jl_maincat.getModel().getElementAt(mainCatArr[kk]));                            
                            t = genQuery.replaceAll("~~~main!!!", catName);
                            if ((kk == mainCatArr.length - 1)) {
                                query = query + t;
                            } else {
                                if (andor.equals("AND")) {
                                    query = query + t + " intersect ";
                                } else {
                                    query = query + t + " union ";
                                }
                            }                            
                        }
                    }
                }
                
                query = "select distinct bname,city,state,stars from hw_business where bid in (" + query + ")";
                
                
                
                    Statement sqlstmt = null;
                    sqlstmt = dbCon.createStatement();
                    rs = sqlstmt.executeQuery(query);
                
                try {
                    remove(jsp_table);
                    table = new JTable(btm(rs));
                    table.addMouseListener(new showReviews());
                    //setPreferredSize(new Dimension(1500, 1500));
                    table.repaint();
                    jsp_table = new JScrollPane(table);
                    //setPreferredSize(new Dimension(1500, 1500));
                    
                    //add(jsp_table, BorderLayout.NORTH);
                    GridBagLayout layout = new GridBagLayout();
                    GridBagConstraints gbc = new GridBagConstraints();                    
                    gbc.gridx = 90;
                    gbc.gridy = yLevel1;
                    gbc.gridwidth = 70;
                    gbc.gridheight = 110;
                    add(jsp_table, gbc);
                    jsp_table.repaint();

                    //setPreferredSize(new Dimension(1500, 1500));

                    setPreferredSize(getPreferredSize());
                    repaint();
                    revalidate();

                } catch (SQLException et) {
                    et.printStackTrace();
                }

            } catch (SQLException et) {
                et.printStackTrace();
            }

        }

    }

    public class GetSubCategoryData implements ListSelectionListener {
        // Executes on change selection in Main Category List:
        @Override
        public void valueChanged(ListSelectionEvent lse) {
            rs = null;
            dl_subcat = new ArrayList();
            
            
            // sql query goals:
            // 1. return sub cat for all selected categories (use and / or)
            // 2. of all businesses that match main cat --> get DOW / Time & populate GUI data list
            
            String andor=jcb_selectChoice.getSelectedItem().toString();
            // read multiple categories:
            int[] catArr = jl_maincat.getSelectedIndices();            
            String tmp1, tmp2, tmp3, tmp4,tmp1a;
            
            String selectedMainCatArr = "";
            
            String query1 = "select distinct sub_cat_name from hw_business_main_category where sub_cat_name is not null and cat_name ='";
            String query2 = "select distinct day_of_week from hw_b_hours where bid in (";
            String query3 = "select distinct from_hr from hw_b_hours where bid in (";
            String query4 = "select distinct to_hr from hw_b_hours where bid in (";
            String query2e = ") order by day_of_week asc";
            String query3e = ") order by from_hr asc";
            String query4e = ") order by to_hr asc";
                        
            String querySubCat = "";
            String queryDOW = "";
            String query_FromHR = "";
            String query_ToHR = "";
            
            if (catArr.length > 0) {
                for (int ii = 0; ii < catArr.length; ii++) {                    
                    if (ii < catArr.length-1) {
                        if (andor.equals("AND")){
                            tmp1 = query1 + String.valueOf(jl_maincat.getModel().getElementAt(catArr[ii])) + "' intersect ";
                            tmp1a = query1 + String.valueOf(jl_maincat.getModel().getElementAt(catArr[ii])) + "') intersect ";
                        } else {
                            tmp1 = query1 + String.valueOf(jl_maincat.getModel().getElementAt(catArr[ii])) + "' union ";
                            tmp1a = query1 + String.valueOf(jl_maincat.getModel().getElementAt(catArr[ii])) + "') union ";                            
                        }
                    } else {
                        tmp1 = query1 + String.valueOf(jl_maincat.getModel().getElementAt(catArr[ii])) + "'";
                        tmp1a = tmp1;
                        //tmp2 = query2 + String.valueOf(jl_maincat.getModel().getElementAt(catArr[ii])) + "'";                        
                        //tmp3 = query3 + String.valueOf(jl_maincat.getModel().getElementAt(catArr[ii])) + "'";
                        //tmp4 = query4 + String.valueOf(jl_maincat.getModel().getElementAt(catArr[ii])) + "'";                        
                    }
                    tmp2 = query2 + tmp1a.replaceFirst("distinct sub_cat_name from", "distinct bid from");
                    tmp3 = query3 + tmp1a.replaceFirst("distinct sub_cat_name from", "distinct bid from");
                    tmp4 = query4 + tmp1a.replaceFirst("distinct sub_cat_name from", "distinct bid from");
                    querySubCat = querySubCat + tmp1;
                    queryDOW = queryDOW + tmp2;
                    query_FromHR = query_FromHR + tmp3;
                    query_ToHR = query_ToHR + tmp4;
                }
            }
            queryDOW = queryDOW + query2e;
            query_FromHR = query_FromHR + query3e;
            query_ToHR = query_ToHR + query4e;
            
            /*
            String str = jl_maincat.getSelectedValue().toString();            
            //String sql2 = "select distinct b_category_sub from business_category where b_category=?";
            String sql2 = "select distinct sub_cat_name from hw_business_main_category where cat_name=?";
            //String sql3 = "select distinct day_of_week,from_hr,to_hr from hw_b_hours bhrs, hw_business_main_category mcat where mcat.bid=bhrs.bid and mcat.cat_name = ?";
            */
            try {                
                Statement sqlstmt = null;
                sqlstmt = dbCon.createStatement();
                rs = sqlstmt.executeQuery(querySubCat);
                
                while (rs.next()) {
                    String subcatg = rs.getString(1);
                    dl_subcat.add(subcatg);
                }
                jl_subcat.setListData(dl_subcat.toArray());
                jsp_subcat.repaint();

            } catch (SQLException e) {
                e.printStackTrace();
            }            
            try {
                // empty out JList of Day of Week
                //jL_dow = new ArrayList();
                //jL_dow.add("Select");                
                Statement sqlstmt = null;
                sqlstmt = dbCon.createStatement();
                rs = sqlstmt.executeQuery(queryDOW);
                jcb_dow.removeAllItems();
                jcb_dow.addItem("---- Select ----");
                while (rs.next()) {
                    String subcatg = rs.getString(1);                    
                    //jL_dow.add(subcatg);
                    jcb_dow.addItem(subcatg);
                }                
                jcb_dow.repaint();

            } catch (SQLException e) {
                e.printStackTrace();
            }
             try {                         
                Statement sqlstmt = null;
                sqlstmt = dbCon.createStatement();
                rs = sqlstmt.executeQuery(query_FromHR);
                jcb_openHour.removeAllItems();
                jcb_openHour.addItem("Select");
                while (rs.next()) {
                    String subcatg = rs.getString(1);
                    jcb_openHour.addItem(subcatg);
                }                
                jcb_openHour.repaint();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                Statement sqlstmt = null;
                sqlstmt = dbCon.createStatement();
                rs = sqlstmt.executeQuery(query_ToHR);
                jcb_closeHour.removeAllItems();
                jcb_closeHour.addItem("Select");
                while (rs.next()) {
                    String subcatg = rs.getString(1);
                    jcb_closeHour.addItem(subcatg);
                }
                jcb_closeHour.repaint();

            } catch (SQLException e) {
                e.printStackTrace();
            }            
        }
    }

    public class GetAttData implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent lse) {
            
            if (null != jl_subcat.getSelectedValue()) {
                // And / Or query build:
                String andor = jcb_selectChoice.getSelectedItem().toString();
                int[] catArr = jl_maincat.getSelectedIndices();
                int[] subCatArr = jl_subcat.getSelectedIndices();
                
                // goal: 
                // 1. get list of attributes
                // 2. update secondary props : DOW, hr
                
                String q1,q2,q3,q4,q5;
                
                // attributes query :                
                String innerQuery = "";                
                String outerQuery = "";
                String m;
                String s;
                if (catArr.length > 0) {
                    for (int jj = 0; jj < catArr.length; jj++) {                        
                        if (subCatArr.length > 0) {
                            //String tmp1 = "select distinct attr_name from hw_business_attributes where bid in ((select distinct bid from hw_business_main_category where sub_cat_name in (";
                            for (int ii = 0; ii < subCatArr.length; ii++) { 
                                m = String.valueOf(jl_maincat.getModel().getElementAt(catArr[jj]));
                                s = String.valueOf(jl_subcat.getModel().getElementAt(subCatArr[ii]));
                                //String tmp2 = "select distinct sub_cat_name from hw_business_main_category where sub_cat_name is not null and cat_name ='" +  + "'";
                                q1 = "select distinct bid from hw_business_main_category where cat_name ='" + m + "' and sub_cat_name in '" + s + "'";
                                if ((ii < subCatArr.length - 1) | (jj < catArr.length - 1)) {
                                    if (andor.equals("AND")) {
                                        q1 = q1 + " intersect ";
                                    } else {
                                        q1 = q1 + " union ";
                                    }
                                }
                                innerQuery = innerQuery + q1;
                            }
                        }
                    } 
                }
                String attQuery = "";
                String dowQuery = "";
                String oTquery = "";
                String cTquery = "";
                
                attQuery = "select distinct attr_name from hw_business_attributes where bid in ( " + outerQuery + ")";
                dowQuery = "select distinct day_of_week from hw_b_hours where bid in( " + outerQuery + ") order by day_of_week asc";
                oTquery = "select distinct from_hr from hw_b_hours where bid in( " + outerQuery + ") order by from_hr asc";
                cTquery = "select distinct to_hr from hw_b_hours where bid in( " + outerQuery + ") order by to_hr asc";
               
                attQuery = "select distinct attr_name from hw_business_attributes where bid in ( " + innerQuery + " )";
                dowQuery = "select distinct day_of_week from hw_b_hours where bid in ( " + innerQuery + " )";
                oTquery = "select distinct to_hr from hw_b_hours where bid in( " + innerQuery + " )";
                cTquery = "select distinct from_hr from hw_b_hours where bid in( " + innerQuery + " )";
                
                
                // old code
                /*
                String mainCatSelect = jl_maincat.getSelectedValue().toString();
                String sql3 = "select  bid from hw_business_main_category where cat_name='" + mainCatSelect + "' and sub_cat_name=";
                String sql2 = "select distinct attr_name from hw_business_attributes a where a.bid in (select  bid from hw_business_main_category where cat_name='" + mainCatSelect + "')";
                                
                
                // multiple selections:
                int[] subCatArr = jl_subcat.getSelectedIndices();                
                String tmp;
                String selectedSubCatArr = "";
                String cquery = "select distinct attr_name from hw_business_attributes a where a.bid in (";        
                if (subCatArr.length > 0) {
                    for (int ii = 0; ii < subCatArr.length; ii++) {                        
                        if (ii < subCatArr.length-1) {
                            if (andor.equals("AND")){
                                tmp = sql3 + "'" + jl_subcat.getModel().getElementAt(subCatArr[ii]) + "' intersect ";
                            } else {
                                tmp = sql3 + "'" + jl_subcat.getModel().getElementAt(subCatArr[ii]) + "' union ";
                            }                            
                        } else {
                            tmp = sql3 + "'" + jl_subcat.getModel().getElementAt(subCatArr[ii]) + "')";
                        }
                        cquery = cquery + tmp;
                    }
                } else {
                    cquery = sql2;
                }
                rs = null;
                dl_att = new ArrayList();
                String str = jl_subcat.getSelectedValue().toString();
                //String sql3 = "select distinct attribute_key from business_attribute a where a.bid in (select distinct business_id from business_category where b_category_sub=?)";
                //String sql3 = "select distinct attr_name from hw_business_attributes a where a.bid in (select distinct bid from hw_business_main_category where cat_name=? and sub_cat_name=?)";
                
                //sql3 = "select distinct attr_name from hw_business_attributes a where a.bid in (select  bid from hw_business_main_category where cat_name='" + jl_maincat.getSelectedValue().toString() + "' and sub_cat_name=";
                */        
                rs = null;
                dl_att = new ArrayList();
                try {                    
                    Statement sqlstmt = null;
                    sqlstmt = dbCon.createStatement();
                    rs = sqlstmt.executeQuery(attQuery);
                    while (rs.next()) {
                        String attr = rs.getString(1);
                        dl_att.add(attr);
                    }
                    jl_att.setListData(dl_att.toArray());
                    jsp_att.repaint();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                
                try {                    
                    Statement sqlstmt = null;
                    sqlstmt = dbCon.createStatement();
                    rs = sqlstmt.executeQuery(dowQuery);
                    jcb_dow.removeAllItems();
                    jcb_dow.addItem("---- Select ----");
                    while (rs.next()) {
                        String subcatg = rs.getString(1);
                        //jL_dow.add(subcatg);
                        jcb_dow.addItem(subcatg);
                    }
                    jcb_dow.repaint();                    
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    Statement sqlstmt = null;
                    sqlstmt = dbCon.createStatement();
                    rs = sqlstmt.executeQuery(oTquery);
                    /*jcb_openHour.removeAllItems();
                    jcb_openHour.addItem("Select");*/
                    jcb_closeHour.removeAllItems();
                    jcb_closeHour.addItem("Select");
                    while (rs.next()) {
                        String subcatg = rs.getString(1);
                       // jcb_openHour.addItem(subcatg); //Changed to fix bug anagha
                        jcb_closeHour.addItem(subcatg);
                    }
                    //jcb_openHour.repaint();
                    jcb_closeHour.repaint();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    Statement sqlstmt = null;
                    sqlstmt = dbCon.createStatement();
                    rs = sqlstmt.executeQuery(cTquery);
                   /* jcb_closeHour.removeAllItems();
                    jcb_closeHour.addItem("Select");*/
                    jcb_openHour.removeAllItems();
                    jcb_openHour.addItem("Select");
                    while (rs.next()) {
                        String subcatg = rs.getString(1);
                       // jcb_closeHour.addItem(subcatg);
                        jcb_openHour.addItem(subcatg);
                    }
                    //jcb_closeHour.repaint();
                    jcb_openHour.repaint();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public DefaultTableModel btm(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);

    }

    

    public class showReviews implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

            try {
                revDiag.remove(diagP);
                String str = table.getValueAt(table.getSelectedRow(), 0).toString();
                //String sql2 = "select distinct r.review_id,r.rev_date,r.stars,r.text,r.user_id,v.useful from votes v, reviews r where  r.business_id in ( select b.business_id from business b where b.business_name like ?) and v.user_id=r.user_id";
                String sql2 = "select distinct r.review_id,r.review_date,r.stars,DBMS_LOB.SUBSTR(r.review_text,4000) as review_text,u.user_name,u.useful_votes from hw_reviews r,hw_yelp_user u where  r.bid in ( select b.business_id from business b where b.business_name like ?) and u.user_id=r.user_id";
                
                stmt = dbCon.prepareStatement(sql2);
                stmt.setString(1, str);
                rs = stmt.executeQuery();

                JTable table1 = new JTable(btm(rs));
                revDiag.setTitle("Reviews Table");
                revDiag.setPreferredSize(new Dimension(600, 600));

                diagP = new JScrollPane(table1);
                revDiag.add(diagP);
                revDiag.pack();
                revDiag.setVisible(true);

                revDiag.setAlwaysOnTop(true);
                revDiag.setVisible(true);
                revDiag.requestFocus();
                revDiag.toFront();
                //revDiag.setFocus(true);

            } catch (SQLException ex) {

                ex.printStackTrace();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

    }

}
