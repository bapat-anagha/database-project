/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HW3;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Class.forName;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;
import java.util.Collection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
//import org.json.JSONObject;

/**
 *
 * @author Anagha
 */
public class populate {

    public static void main(String[] args) throws SQLException {
        // TODO Auto-generated method stub
        
        // Get command line args:
        boolean globalUseFlag = true;
        String businessJsonFile;
        String ReviewsJsonFile;
        String CheckInJsonFile;
        String UserJsonFile;   
        
        if (globalUseFlag == true){
            businessJsonFile = args[0];
            ReviewsJsonFile = args[1];
            CheckInJsonFile = args[2];
            UserJsonFile = args[3];        
        } else {
            businessJsonFile = "yelp_business.json";
            ReviewsJsonFile = "yelp_review.json";
            CheckInJsonFile = "";
            UserJsonFile = "yelp_user.json";
        }      
        
         
        /* --------------------- setup sql dB connection --------------------------*/
        System.out.print("Connecting to DB ... ");
        Connection_DB dbConnection = new Connection_DB();
        Connection dbCon = dbConnection.get_DBConn();
        System.out.println("done !");
        
        if (true){
            //Deleting the previous data.
            System.out.print("Deleting previous data ... ");
            delete_data(dbCon);
            System.out.println("done");

            //Adding data to tbls
            System.out.print("Loading data for business ");       
            String jsonFilePath = "C:\\Users\\vithakar\\Desktop\\YelpDataset\\" + businessJsonFile;
            loadBusinessData(jsonFilePath, dbCon);
            System.out.println("done !");

            System.out.print("Loading data for user ... ");
            jsonFilePath = "C:\\Users\\vithakar\\Desktop\\YelpDataset\\" + UserJsonFile;
            loadUserData(jsonFilePath, dbCon);
            System.out.println("done !");

            System.out.print("Loading Data for reviews ");
            jsonFilePath = "C:\\Users\\vithakar\\Desktop\\YelpDataset\\" + ReviewsJsonFile;
            loadReviewData(jsonFilePath, dbCon);
            System.out.println("done !");
        }
        // close dB connection 
        if (dbCon != null) {
            dbCon.close();
            System.out.println("close DB connection");
        }
    }

    public static void delete_data(Connection dbCon) throws SQLException {
        try {
            Statement stmt = dbCon.createStatement();
            String query = "";
            
            System.out.print("\tDeleting all data from 'HW_FRIENDS' ... ");
            query = "delete from hw_friends";
            stmt.executeUpdate(query);            
            System.out.println("Done");
            
            System.out.print("\tDeleting all data from table 'HW_B_HOURS' ... ");
            query = "delete from hw_b_hours";
            stmt.executeUpdate(query);
            System.out.println("Done");

            System.out.print("\tDeleting all data from table 'HW_BUSINESS_ATTRIBUTES' ... ");
            query = "delete from hw_business_attributes";
            stmt.executeUpdate(query);
            System.out.println("Done");

            System.out.print("\tDeleting all data from table 'HW_BUSINESS_MAIN_CATEGORY' ... ");
            query = "delete from hw_business_main_category";
            stmt.executeUpdate(query);
            System.out.println("Done");

           /* System.out.print("\tDeleting all data from table 'HW_BUSINESS_SUB_CATEGORY' ... ");
            query = "delete from hw_business_sub_category";
            stmt.executeUpdate(query);
            System.out.println("Done");*/

            System.out.print("\tDeleting all data from table 'HW_REVIEWS' ... ");
            query = "delete from hw_reviews";
            stmt.executeUpdate(query);
            System.out.println("Done");

            System.out.print("\tDeleting all data from table : 'HW_BUSINESS' ... ");
            query = "delete from hw_business";
            stmt.executeUpdate(query);
            System.out.println("Done");

            System.out.print("\tDeleting all data from table : 'HW_YELP_USER' ... ");
            query = "delete from hw_yelp_user";
            stmt.executeUpdate(query);
            System.out.println("Done");
        } catch (SQLException s) {
            System.out.println("Error");
            s.printStackTrace();
            dbCon.close();
        }
    }

    
      public static void loadBusinessData(String jsonFilePath, Connection dbCon) throws SQLException {
        PreparedStatement stmt, stmt2, stmt3, stmt4 = null;
        boolean eof = false;
        FileReader fread = null;
        BufferedReader fbuff = null;
        try {
            String currLine;
            fread = new FileReader(jsonFilePath);
            fbuff = new BufferedReader(fread);
            JSONParser jsonParser = new JSONParser();

            while (!eof) {
                currLine = fbuff.readLine(); // read line by line
                // check for end of file
                if (currLine == null) {
                    eof = true;
                } else {

                    // Generate JSON object
                    Object jsonData = jsonParser.parse(currLine);
                    JSONObject jsonDataObject = (JSONObject) jsonData;

                    // Extract Data from JSON Object
                    String isopen = "";
                    String busID = (String) jsonDataObject.get("business_id");
                    String busAdd = (String) jsonDataObject.get("full_address");
                    String busCity = (String) jsonDataObject.get("city");
                    String busName = (String) jsonDataObject.get("name");
                    String busState = (String) jsonDataObject.get("state");
                    String busType = (String) jsonDataObject.get("type");
                    Boolean busIsOpen = (Boolean) jsonDataObject.get("open");
                    long busReviewCnt = (long) jsonDataObject.get("review_count");
                    double busLong = (double) jsonDataObject.get("longitude");
                    double busLat = (double) jsonDataObject.get("latitude");
                    double busStars = (double) jsonDataObject.get("stars");
                    if (busIsOpen == true) {
                        isopen = "true";
                    } else {
                        isopen = "false";
                    }
                    String sqlquery = "INSERT INTO HW_BUSINESS"
                            + "(bid, full_address, is_open, city, review_count, bname, longitude, state, stars, latitude, btype) VALUES"
                            + "(?,?,?,?,?,?,?,?,?,?,?)";

                    stmt = dbCon.prepareStatement(sqlquery);

                    stmt.setString(1, busID);
                    stmt.setString(2, busAdd);
                    stmt.setString(3, isopen);
                    stmt.setString(4, busCity);
                    stmt.setLong(5, busReviewCnt);
                    stmt.setString(6, busName);
                    stmt.setDouble(7, busLong);
                    stmt.setString(8, busState);
                    stmt.setDouble(9, busStars);
                    stmt.setDouble(10, busLat);
                    stmt.setString(11, busType);
                    stmt.executeUpdate();
                    stmt.close();
                }
            }
        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
        } catch (IOException ioError) {
            ioError.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        } catch (SQLException sqlErr) {
            sqlErr.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

        try {
            eof = false;
            String currLine;
            fread = new FileReader(jsonFilePath);
            fbuff = new BufferedReader(fread);
            JSONParser jsonParser = new JSONParser();

            while (!eof) {
                currLine = fbuff.readLine(); // read line by line
                // check for end of file
                if (currLine == null) {
                    eof = true;
                } else {
                    // Generate JSON object
                    Object jsonData = jsonParser.parse(currLine);
                    JSONObject jsonDataObject = (JSONObject) jsonData;

                    if (jsonDataObject.get("hours") != null) {
                        Object busHours = jsonDataObject.get("hours");
                        String t_open = "";
                        String t_close = "";
                        String bid = (String) jsonDataObject.get("business_id");

                        JSONObject busHoursObjColl = (JSONObject) busHours;
                        Collection keys_collection = busHoursObjColl.keySet();

                        Object[] ObjKeys = keys_collection.toArray();

                        for (int i = 0; i < ObjKeys.length; i++) {
                            String attrkey = (String) ObjKeys[i];
                            // String attrvalue = (String) nestedObject.get(keyType[i]).toString();
                            if (busHoursObjColl.get(ObjKeys[i]) instanceof JSONObject) {
                                String day_of_week = (String) ObjKeys[i];
                                JSONObject inner_obj = (JSONObject) busHoursObjColl.get(day_of_week);

                                Collection keyColl_inner = inner_obj.keySet();
                                Object[] keyType_inner = keyColl_inner.toArray();
                                for (int j = 0; j < keyType_inner.length; j++) {
                                    attrkey = (String) keyType_inner[j];
                                    if (attrkey.equals("open")) {
                                        t_open = (String) inner_obj.get(keyType_inner[j]).toString();
                                    } else {
                                        t_close = (String) inner_obj.get(keyType_inner[j]).toString();
                                    }
                                }
                                stmt2 = dbCon.prepareStatement("insert into hw_b_hours (bid,day_of_week,from_hr,to_hr) values(?, ?, ? ,?)");
                                stmt2.setString(1, bid);
                                stmt2.setString(2, day_of_week);
                                stmt2.setString(3, t_open);
                                stmt2.setString(4, t_close);
                                stmt2.executeUpdate();
                                stmt2.close();
                            }
                        }
                    }
                }

            }

        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
        } catch (IOException ioError) {
            ioError.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        } catch (SQLException sqlErr) {
            sqlErr.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

        try {
            eof = false;
            String currLine;
            fread = new FileReader(jsonFilePath);
            fbuff = new BufferedReader(fread);
            JSONParser jsonParser = new JSONParser();

            while (!eof) {
                currLine = fbuff.readLine(); // read line by line
                // check for end of file
                if (currLine == null) {
                    eof = true;
                } else {
                    Object jsonData = jsonParser.parse(currLine);
                    JSONObject jsonDataObject = (JSONObject) jsonData;
                    if (jsonDataObject.get("attributes") != null) {
                        Object attr = jsonDataObject.get("attributes");
                        JSONObject attributeObjColl = (JSONObject) attr;
                        Collection keys_collection = attributeObjColl.keySet();
                        Object[] ObjKeys = keys_collection.toArray();

                        for (int i = 0; i < ObjKeys.length; i++) {
                            String attrkey = (String) ObjKeys[i];
                            String attrvalue = (String) attributeObjColl.get(ObjKeys[i]).toString();

                            String bid = (String) jsonDataObject.get("business_id");

                            if (attributeObjColl.get(ObjKeys[i]) instanceof JSONObject) {
                                String temp = (String) ObjKeys[i];
                                org.json.simple.JSONObject nestedObjectOne = (JSONObject) attributeObjColl.get(temp);

                                Collection keyCollOne = nestedObjectOne.keySet();
                                Object[] keyTypeOne = keyCollOne.toArray();
                                for (int j = 0; j < keyTypeOne.length; j++) {
                                    attrkey = temp + " " + keyTypeOne[j];
                                    attrvalue = (String) nestedObjectOne.get(keyTypeOne[j]).toString();

                                    stmt3 = dbCon.prepareStatement("insert into hw_business_attributes (bid,attr_name) values(?, ?)");
                                    stmt3.setString(1, bid);
                                    stmt3.setString(2, attrkey);
                                    stmt3.executeUpdate();
                                    stmt3.close();
                                }

                            } else {
                                stmt3 = dbCon.prepareStatement("insert into hw_business_attributes (bid,attr_name) values(?, ?)");
                                stmt3.setString(1, bid);
                                stmt3.setString(2, attrkey);
                                stmt3.executeUpdate();
                                stmt3.close();
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
        } catch (IOException ioError) {
            ioError.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        } catch (SQLException sqlErr) {
            sqlErr.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

        try {
            eof = false;
            String currLine;
            fread = new FileReader(jsonFilePath);
            fbuff = new BufferedReader(fread);
            JSONParser jsonParser = new JSONParser();

            while (!eof) {
                currLine = fbuff.readLine(); // read line by line
                // check for end of file
                if (currLine == null) {
                    eof = true;
                } else {
                    Object jsonData = jsonParser.parse(currLine);
                    JSONObject jsonDataObject = (JSONObject) jsonData;
                    if (jsonDataObject.get("categories") != null) {
                        String bus_id = (String) jsonDataObject.get("business_id");
                        JSONArray cat_arr = (JSONArray) jsonDataObject.get("categories");
                        String cat_main = "";
                        String sub_cat = "";
                        if (cat_arr.size() == 1) {
                            cat_main = (String) cat_arr.get(0);

                            stmt4 = dbCon.prepareStatement("Insert into hw_business_main_category (bid,cat_name,sub_cat_name) values (?,?,?)");
                            stmt4.setString(1, bus_id);
                            stmt4.setString(2, cat_main);
                            //stmt.setString(3, "null");
                            stmt4.setString(3, "");

                            stmt4.executeUpdate();
                            stmt4.close();

                        } else {
                            cat_main = (String) cat_arr.get(cat_arr.size() - 1);
                            for (int i = 0; i < (cat_arr.size() - 1); i++) {
                                sub_cat = (String) cat_arr.get(i);
                                //System.out.println(bid + "  " + last + "  " + two);

                                stmt4 = dbCon.prepareStatement("Insert into hw_business_main_category (bid,cat_name,sub_cat_name) values (?,?,?)");
                                stmt4.setString(1, bus_id);
                                stmt4.setString(2, cat_main);
                                stmt4.setString(3, sub_cat);

                                stmt4.executeUpdate();
                                stmt4.close();

                            }
                        }

                    }
                }

            }
        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
        } catch (IOException ioError) {
            ioError.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        } catch (SQLException sqlErr) {
            sqlErr.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    
    public static void loadUserData(String jsonFilePath, Connection dbCon) throws SQLException {

        PreparedStatement stmt1, stmt2 = null; // init sql statement object
        boolean eof = false;
        FileReader fread = null;
        BufferedReader fbuff = null;
        try {
            
            //eof = false;
            String currLine;
            fread = new FileReader(jsonFilePath);
            fbuff = new BufferedReader(fread);
            JSONParser jsonParser = new JSONParser();
            //long count= 0;
            while (!eof) {                
               /* if (count == 1000 ){
                    System.out.println(count);
                    count = 0;
                }   
                count++;*/
                currLine = fbuff.readLine(); // read line by line
                // check for end of file
                if (currLine == null) {
                    eof = true;
                } else {
                    Object jsonData = jsonParser.parse(currLine);
                    JSONObject jsonDataObject = (JSONObject) jsonData;

                    String yelping_since = (String) jsonDataObject.get("yelping_since");
                    JSONObject votes = (JSONObject) jsonDataObject.get("votes");

                    long funny_votes = (long) votes.get("funny");
                    long useful_votes = (long) votes.get("useful");
                    long cool_votes = (long) votes.get("cool");

                    int vote_funny = (int) funny_votes;
                    int vote_useful = (int) useful_votes;
                    int vote_cool = (int) cool_votes;

                    long lreview_count = (long) jsonDataObject.get("review_count");
                    int review_count = (int) lreview_count;
                    String name = (String) jsonDataObject.get("name");
                    String user_id = (String) jsonDataObject.get("user_id");

                    long lfan = (long) jsonDataObject.get("fans");
                    int fan = (int) lfan;
                    double average_stars = (double) jsonDataObject.get("average_stars");
                    String u_type = (String) jsonDataObject.get("type");
                    JSONObject compliments = (JSONObject) jsonDataObject.get("compliments");

                    int hot_c = 0, more_c = 0, profile_c = 0, cute_c = 0, list_c = 0, note_c = 0, plain_c = 0, cool_c = 0, funny_c = 0, write_c = 0, photo_c = 0;
                    if (compliments.get("hot") != null) {
                        long hot_cl = (long) compliments.get("hot");
                        hot_c = (int) hot_cl;
                    }

                    if (compliments.get("more") != null) {
                        long more_cl = (long) compliments.get("more");
                        more_c = (int) more_cl;
                    }
                    if (compliments.get("profile") != null) {
                        long profile_cl = (long) compliments.get("profile");
                        more_c = (int) profile_cl;
                    }

                    if (compliments.get("cute") != null) {
                        long cute_cl = (long) compliments.get("cute");
                        cute_c = (int) cute_cl;
                    }

                    if (compliments.get("list") != null) {
                        long list_cl = (long) compliments.get("list");
                        list_c = (int) list_cl;
                    }

                    if (compliments.get("note") != null) {
                        long note_cl = (long) compliments.get("note");
                        note_c = (int) note_cl;
                    }

                    if (compliments.get("plain") != null) {
                        long plain_cl = (long) compliments.get("plain");
                        plain_c = (int) plain_cl;
                    }

                    if (compliments.get("cool") != null) {
                        long cool_cl = (long) compliments.get("cool");
                        cool_c = (int) cool_cl;
                    }

                    if (compliments.get("funny") != null) {
                        long funny_cl = (long) compliments.get("funny");
                        funny_c = (int) funny_cl;
                    }

                    if (compliments.get("writer") != null) {
                        long write_cl = (long) compliments.get("writer");
                        write_c = (int) write_cl;
                    }

                    if (compliments.get("photos") != null) {
                        long photo_cl = (long) compliments.get("photos");
                        photo_c = (int) photo_cl;

                    }

                    stmt1 = dbCon.prepareStatement("insert into hw_yelp_user (yelping_since, funny_votes, useful_votes, cool_votes, review_count, user_name, user_id, fans, average_stars, user_type, hot_compliment, more_compliment, profile_compliment, cute_compliment, list_compliment, note_compliment, plain_compliment, cool_compliment, funny_compliment, writer_compliment, photos_compliment) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    stmt1.setString(1, yelping_since);
                    stmt1.setInt(2, vote_funny);
                    stmt1.setInt(3, vote_useful);
                    stmt1.setInt(4, vote_cool);
                    stmt1.setInt(5, review_count);
                    stmt1.setString(6, name);
                    stmt1.setString(7, user_id);
                    stmt1.setInt(8, fan);
                    stmt1.setDouble(9, average_stars);
                    stmt1.setString(10, u_type);
                    stmt1.setInt(11, hot_c);
                    stmt1.setInt(12, more_c);
                    stmt1.setInt(13, profile_c);
                    stmt1.setInt(14, cute_c);
                    stmt1.setInt(15, list_c);
                    stmt1.setInt(16, note_c);
                    stmt1.setInt(17, plain_c);
                    stmt1.setInt(18, cool_c);
                    stmt1.setInt(19, funny_c);
                    stmt1.setInt(20, write_c);
                    stmt1.setInt(21, photo_c);

                    stmt1.executeUpdate();
                    stmt1.close();
                    //System.out.println(" ... db entry complete");
                    if (jsonDataObject.get("friends") != null) {
                        JSONArray array = (JSONArray) jsonDataObject.get("friends");
                        stmt2 = dbCon.prepareStatement("Insert into hw_friends values (?,?)");
                        for (int i = 0; i < array.size(); i++) {
                            String frnd_id = (String) array.get(i);
                            stmt2.setString(1, user_id);
                            stmt2.setString(2, frnd_id);
                            stmt2.executeUpdate();
                        }
                        stmt2.close();                    
                    }
                }
            }
        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
        } catch (IOException ioError) {
            ioError.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        } catch (SQLException sqlErr) {
            sqlErr.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt2 != null) {
                stmt2.close();
            }
        }
    }

     public static void loadReviewData(String jsonFilePath, Connection dbCon) throws SQLException {
        PreparedStatement stmt10 = dbCon.prepareStatement("insert into hw_reviews (review_id,funny_vote,useful_vote,cool_vote,user_id,stars,review_date,review_text,review_type, bid) VALUES (?,?,?,?,?,?,?,?,?,?)");
        boolean eof = false;
        FileReader fread = null;
        BufferedReader fbuff = null;
        try {
            String currLine;
            fread = new FileReader(jsonFilePath);
            fbuff = new BufferedReader(fread);
            JSONParser jsonParser = new JSONParser();
            while (!eof) {
                currLine = fbuff.readLine(); // read line by line
                // check for end of file
                if (currLine == null) {
                    eof = true; // if reached eof exit while loop
                } else {
                    Object jsonData = jsonParser.parse(currLine);
                    JSONObject jsonDataObject = (JSONObject) jsonData;

                    String user_id = (String) jsonDataObject.get("user_id");
                    String review_id = (String) jsonDataObject.get("review_id");
                    JSONObject votes = (JSONObject) jsonDataObject.get("votes");
                    int funny_vote;
                    int useful_vote;
                    int cool_vote;
                    funny_vote = (int) ((long) votes.get("funny"));
                    useful_vote = (int) ((long) votes.get("useful"));
                    cool_vote = (int) ((long) votes.get("cool"));

                    String business_id = (String) jsonDataObject.get("business_id");
                    int stars = (int) ((long) jsonDataObject.get("stars"));
                    String date = (String) jsonDataObject.get("date");
                    String text = ((String) jsonDataObject.get("text").toString());
                    String type = ((String) jsonDataObject.get("type").toString());

                    stmt10.setString(1, review_id);
                    stmt10.setInt(2, funny_vote);
                    stmt10.setInt(3, useful_vote);
                    stmt10.setInt(4, cool_vote);
                    stmt10.setString(5, user_id);
                    stmt10.setInt(6, stars);
                    stmt10.setString(7, date);
                    stmt10.setString(8, text);
                    stmt10.setString(9, type);
                    stmt10.setString(10, business_id);
                    stmt10.executeUpdate();

                }

            }
            stmt10.close();
        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
        } catch (IOException ioError) {
            ioError.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        } catch (SQLException sqlErr) {
            sqlErr.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt10 != null) {
                stmt10.close();
            }
        }
    }
}


