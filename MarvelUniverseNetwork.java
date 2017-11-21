package marvel.universe.network;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.HashMap;
import com.csvreader.CsvReader; //www.csvreader.com

public class MarvelUniverseNetwork  {
 
   public static void main(String[] args)   {
       int heroCount = 0;               //keep track of number of heros
       int comicCount = 0;              //keep track of number of comics
       int totalAppearances = 0;        //keep track of instances of heros appearing in comics
       int totalPartnerships = 0;       //keep track of instances of partnerships
       double avgHeroes;                //hold the average heroes per comic
       double avgComics;                //hold the average comics per hero
       double avgPartnerships;          //hold the average partnerships per hero
       
       //create a map of hashsets to successfully parse the hero network data
       HashMap<String,HashSet<String>> networkMap = new HashMap();
   
       try  {
            CsvReader nodes = new CsvReader("nodes.csv");//open the file 
            nodes.readHeaders();    //read in the names of the headers for easy parse
        
            while (nodes.readRecord())  {   //while loop until records are all parsed
                String nodeName = nodes.get("node");    //get string from node column, put into variable
                String nodeType = nodes.get("type");    //get string from type column, put into variable
                
                if(nodeType.equals("comic")) {
                    ++comicCount;     //increment comics
                }  //end if
                else if(nodeType.equals("hero")) {
                    ++heroCount;      //increment heros
                }   //end else if
            }   //end while loop
            
            CsvReader edges = new CsvReader("edges.csv");//open the file
            edges.readHeaders();    //read the names of the headers
            
            while (edges.readRecord())  {   //count the total instances of heros appearing in comics
                ++totalAppearances; 
            }
            
            CsvReader partners = new CsvReader("hero-network.csv"); //open the file
            partners.readHeaders(); //read in the header
          
            while (partners.readRecord())   {
                String hero1 = partners.get("hero1");   //get first hero's name
                String hero2 = partners.get("hero2");   //get partner's name
                
                //the following code makes sure every partnership is added both ways
                //this remedies the deficiencies in the hero-network file.
                if(networkMap.containsKey(hero1))   {
                    if(!networkMap.get(hero1).contains(hero2))  {
                        networkMap.get(hero1).add(hero2);   //if the hero1 exists, add to the set 
                        ++totalPartnerships;
                    }
                }
                else    {
                    HashSet<String> heroSet = new HashSet<>();
                    networkMap.put(hero1, heroSet);     //add the set to the hash map
                    networkMap.get(hero1).add(hero2);   //add hero 2 to the hash set
                    ++totalPartnerships;
                }
                if(networkMap.containsKey(hero2))   {
                    if(!networkMap.get(hero2).contains(hero1))  {
                        networkMap.get(hero2).add(hero1);//if hero 2 exists, add to their set
                        ++totalPartnerships;
                    }
                }
                else    {
                    HashSet<String> heroSet = new HashSet<>();
                    networkMap.put(hero2, heroSet);     //add the set to the hash map
                    networkMap.get(hero2).add(hero1);   //add hero1 to the hash set
                    ++totalPartnerships;
                }
            } 
            //totalPartnerships = totalPartnerships / 2;  //eliminate double counted partnerships
       }    catch (FileNotFoundException e)    {
                System.out.println("File not found");
       }    catch (IOException e)   {
                System.out.println("IOException caught");
       }
       //avgComics per hero = divide the total instances of heros appearing by the number of heros
       avgComics = ((double)totalAppearances / (double)heroCount);     //calculate the average comics per hero
       //avgHeros per comic = divide the total number of appearances by the number of comics
       avgHeroes = ((double)totalAppearances / (double)comicCount);
       //avgPartnerships = divide total number of partnership instances by total number of heroes
       avgPartnerships = ((double)totalPartnerships / (double)heroCount);
       
       //print all of the results, round the doubles
       System.out.println("Number of heroes: " + heroCount);
       System.out.println("Number of comics: " + comicCount);
       System.out.println("Mean heroes per comic: " + (Math.round(avgHeroes*100.0) / 100.0));
       System.out.println("Mean comics per hero: " + (Math.round(avgComics*100.0) / 100.0));
       System.out.println("Mean partners per hero: " + (Math.round(avgPartnerships*100.0) / 100.0));
    }
}
