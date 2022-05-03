package com.example.finalproject.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.example.finalproject.Credentials;
import com.example.finalproject.RetrieveData;
import com.example.finalproject.model.CSVData;
import com.example.finalproject.model.GenericResult;
import com.example.finalproject.model.Names;
import com.example.finalproject.services.GenericResultService;
import com.example.finalproject.services.NamesService;
import com.example.finalproject.services.ResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * This is the controller class, where the endpoints are built and the
 * operations are coordinated from.
 */
@Controller
public class MVController {

    /**
     * NamesService object.
     */
    @Autowired
    private NamesService namesService;

    /**
     * GenericResultService object.
     */
    @Autowired
    private GenericResultService genericResultsService;

    /**
     * ResponseService object.
     */
    @Autowired
    private ResponseService responseService;

    /**
     * Landing page endpoint, where the 'index.html' file is rendered with the names
     * and
     * generic objects injected.
     * 
     * @return ModelAndView index.html.
     */
    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView();
        List<Names> names = namesService.getAllNames();
        List<GenericResult> gen = genericResultsService.getAllData();
        mav.addObject("names", names);
        mav.addObject("generic", gen);
        mav.setViewName("index");
        return mav;
    }

    /**
     * Charts endpoint, where the 'charts.html' file is rendered with the names and
     * generic objects injected.
     * 
     * @return ModelAndView charts.html.
     */
    @RequestMapping("/charts")
    public ModelAndView charts() {
        ModelAndView mav = new ModelAndView();
        List<Names> names = namesService.getAllNames();
        List<GenericResult> gen = genericResultsService.getAllData();
        mav.addObject("names", names);
        mav.addObject("generic", gen);
        mav.setViewName("charts");
        return mav;
    }

    /**
     * New Data endpoint, where the 'newdata.html' file is rendered.
     * 
     * @return ModelAndView newdata.html.
     */
    @RequestMapping("/newdata")
    public ModelAndView newdata() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("newdata");
        return mav;
    }

    /**
     * Delete data endpoint, where the 'deletedata.html' file is rendered.
     * 
     * @return ModelAndView deletedata.html.
     */
    @RequestMapping("/deletedata")
    public ModelAndView deletedata() {
        ModelAndView mav = new ModelAndView();
        List<Names> names = namesService.getAllNames();
        mav.addObject("names", names);
        mav.setViewName("deletedata");
        return mav;
    }

    /**
     * Reset endpoint, whete the 'reset.html' file is rendered.
     * 
     * @return ModelAndView reset.html.
     */
    @RequestMapping("/reset")
    public ModelAndView reset() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("reset");
        return mav;
    }

    /**
     * Reload endpoint, where the 'reload.html' file is rendered.
     * 
     * @return ModelAndView reload.html.
     */
    @RequestMapping("/reload")
    public ModelAndView reload() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("reload");
        return mav;
    }

    /**
     * Reload Database POST endpoint, where the stored procedure responsible for
     * reseting the whole database is called and the repopulation of the tables is
     * performed.
     * 
     * @return ResponseEntity with HttpStatus, either OK if successful, or
     *         BAD_REQUEST otherwise.
     * @throws IOException
     */
    @PostMapping("/reloadDatabase")
    public ResponseEntity<Object> reloadDatabase() throws IOException {
        RetrieveData rd;
        Credentials credentials = new Credentials();
        HashMap<String, String> credentialsMap = credentials.getCredentials();
        String dbName = credentialsMap.get("dbName");
        String user = credentialsMap.get("user");
        String pwd = credentialsMap.get("pwd");
        try {
            genericResultsService.resetDatabase(); // Reset database.
            rd = new RetrieveData(dbName, user, pwd); // New database connection
            rd.loadDatabase(); // Load the database with data from API.
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (SQLException | IOException e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Reset Database POST endpoint, where the stored procedure responsible for
     * reseting the whole database is called.
     * 
     * @return ResponseEntity with HttpStatus, either OK if successfull, or
     *         BAD_REQUEST otherwise.
     */
    @PostMapping("/resetDatabase")
    public ResponseEntity<Object> resetDatabase() {
        boolean reset = genericResultsService.resetDatabase();
        if (reset) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Delete item POST endpoint, where the stored procedure that deletes a
     * supernova by ID is called.
     * 
     * @param name the name of the supernova to be deleted from the database.
     * @return ResponseEntity with HttpStatus, either OK if successfull, or
     *         BAD_REQUEST otherwise.
     */
    @PostMapping("/deleteItem")
    public ResponseEntity<Object> deleteItem(@RequestBody Names name) {
        boolean res = namesService.deleteById(name.getId());
        HashMap<String, Boolean> map = new HashMap<>();
        map.put("message", res);

        if (res) {
            return new ResponseEntity<Object>(map, HttpStatus.OK);
        }

        return new ResponseEntity<Object>(map, HttpStatus.BAD_REQUEST);
    }

    /**
     * Add New Data POST endpoint, where the stored procedure to insert the new data
     * is called.
     * 
     * @param data the data parsed from the csv file.
     * @return ResponseEntity with HttpStatus, either OK if successfull, or
     *         BAD_REQUEST otherwise.
     */
    @PostMapping("/addNewData")
    public ResponseEntity<Object> addNewData(@Valid @RequestBody CSVData data) {
        HashMap<String, String> map = new HashMap<>();
        map.put("message", "Successfull submission!");

        String name = data.getName();
        List<Names> allNames = namesService.getAllNames();

        // If such supernova does not exist in the database, insert.
        // Otherwise, do not insert.
        for (int i = 0; i < allNames.size(); i++) {
            if (allNames.get(i).getName().equals(name)) {
                map.replace("message", "Supernova already exists in the database!");
                return new ResponseEntity<Object>(map, HttpStatus.BAD_REQUEST);
            }
        }

        responseService.insertData(data);
        return new ResponseEntity<Object>(map, HttpStatus.OK);
    }

    /**
     * Get SN Data GET endpoint, where the stored procedure responsible for getting
     * the supernova data by name is called.
     * 
     * @param name the name of the supernova to be queried.
     * @return ResponseEntity with HttpStatus, either OK if successfull, or
     *         BAD_REQUEST otherwise.
     */
    @RequestMapping("/getSNData/{name}")
    public ResponseEntity<Object> getData(@PathVariable String name) {
        Map<String, Object> map = new HashMap<>();
        List<GenericResult> gen = genericResultsService.getDataByName(name);

        if (!gen.isEmpty()) {
            map.put("data", gen);
            return new ResponseEntity<Object>(map, HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(map, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get SN Data GET endpoint, where the stored procedure responsible for getting
     * all the data from the database is called.
     * 
     * @return ResponseEntity with HttpStatus, either OK if successfull, or
     *         BAD_REQUEST otherwise.
     */
    @RequestMapping("/getSNData")
    public ResponseEntity<Object> getTotalData() {
        Map<String, Object> map = new HashMap<>();
        List<GenericResult> gen = genericResultsService.getAllData();
        if (!gen.isEmpty()) {
            map.put("data", gen);
            return new ResponseEntity<Object>(map, HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(map, HttpStatus.BAD_REQUEST);
        }
    }

}
