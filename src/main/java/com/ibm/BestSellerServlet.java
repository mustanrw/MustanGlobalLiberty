/*
The MIT License (MIT)

Copyright (c) 2015 IBM

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package com.ibm;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.nytimes.BestSellerList;
import com.ibm.nytimes.NewYorkTimes;



/**
 * Servlet implementation class BestSellers
 */
@WebServlet(urlPatterns = {"/BestSellers"})
public class BestSellerServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(BestSellerServlet.class);
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public BestSellerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/json");
    	response.setCharacterEncoding("UTF-8");

    	OutputStream stream = response.getOutputStream();
    	OutputStreamWriter writer = new OutputStreamWriter(stream, "UTF-8");

    	String listName = request.getParameter("list");
    	String date = request.getParameter("date");

    	logger.debug("Requested list {} and requested date {}", listName, date);

		NewYorkTimes times = new NewYorkTimes(listName, date);

		try {
			BestSellerList bestSellers = times.getList();
			ObjectMapper mapper = new ObjectMapper();

			String listContents = mapper.writeValueAsString(bestSellers.getBooks());
			logger.debug("Booklist is {}", listContents);
			writer.write(listContents);
			writer.flush();
			writer.close();
		}
		catch(Exception e) {
			logger.error("New York times list unavailable");
			throw new IOException("Could not get book list from ny times");
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
