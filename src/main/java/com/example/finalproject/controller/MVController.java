package com.example.finalproject.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

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

@Controller
public class MVController {

    @Autowired
    private NamesService namesService;

    @Autowired
    private GenericResultService genericResultsService;

    @Autowired
    private ResponseService responseService;

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

    @RequestMapping("/newdata")
    public ModelAndView newdata() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("newdata");
        return mav;
    }

    @RequestMapping("/deletedata")
    public ModelAndView deletedata() {
        ModelAndView mav = new ModelAndView();
        List<Names> names = namesService.getAllNames();
        mav.addObject("names", names);
        mav.setViewName("deletedata");
        return mav;
    }

    @RequestMapping("/reset")
    public ModelAndView reset() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("reset");
        return mav;
    }

    @RequestMapping("/reload")
    public ModelAndView reload() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("reload");
        return mav;
    }

    @PostMapping("/reloadDatabase")
    public ResponseEntity<Object> reloadDatabase() {
        RetrieveData rd;
        try {
            genericResultsService.resetDatabase();
            rd = new RetrieveData("SUPERNOVAE", "root", "root");
            rd.loadDatabase();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (SQLException | IOException e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/resetDatabase")
    public ResponseEntity<Object> resetDatabase() {
        boolean reset = genericResultsService.resetDatabase();
        if (reset) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

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

    @RequestMapping("/getSNData/{name}")
    public ResponseEntity<Object> getData(@PathVariable String name) {
        Map<String, Object> map = new HashMap<>();
        List<GenericResult> gen = genericResultsService.getDataByName(name);
        map.put("data", gen);
        return new ResponseEntity<Object>(map, HttpStatus.OK);
    }

    @RequestMapping("/getSNData")
    public ResponseEntity<Object> getTotalData() {
        Map<String, Object> map = new HashMap<>();
        List<GenericResult> gen = genericResultsService.getAllData();
        map.put("values", gen);
        return new ResponseEntity<Object>(map, HttpStatus.OK);
    }

}
