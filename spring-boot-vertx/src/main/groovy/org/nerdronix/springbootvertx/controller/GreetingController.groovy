package org.nerdronix.springbootvertx.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.context.request.async.DeferredResult
import org.vertx.groovy.core.eventbus.EventBus;
import org.vertx.groovy.core.http.HttpClient

/**
 * 
 * A simple controller class which accepts http requests.
 * @author Abraham Menacherry
 *
 */
@Controller
public class GreetingController {
	
	/**
	 * An async vertx http client. It can be used for non-blocking calls to 3rd party json's.
	 */
	@Autowired
	HttpClient client
	
	@Autowired
	EventBus eventBus;
	
	/**
	 * Greeting which is specific to spring boot. No vertx
	 * @param name reflected back to user using the thymeleaf template in resources folder.
	 * @param model
	 * @return
	 */
	@RequestMapping('/greeting')
	public String greeting(@RequestParam(value = 'name', required = false, defaultValue = 'you') String name, Model model)
	{
		model.addAttribute('name', name)
		return 'greeting'
	}

	/**
	 * Accepts an http request and sends it on to url http://jsonplaceholder.typicode.com/posts/1, which is a 3rd party json hosted publicly on the net. 
	 * This is an async service and it also uses the vertx http client for sending the request in a non-blocking fashion.
	 * @param id This should be 1. Only used for demonstration purposes.
	 * @return A deferred result, i.e it is an Async service. 
	 */
	@RequestMapping(value = '/posts/{id}', method = RequestMethod.GET)
	public DeferredResult<ResponseEntity<String>> thirdPartyJson(@PathVariable("id") long id) {	
		DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>()
		// send it out to 3rd party json provider asynchronously
		client.getNow("/posts/${id}") { resp ->
			println "Got response ${resp.statusCode}"
			resp.bodyHandler { body ->
				// Publish messages via event bus
				eventBus.publish("hello.listeners", body.toString())
				HttpHeaders headers = new HttpHeaders()
				headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE)
				ResponseEntity<String> responseEntity = new ResponseEntity<>(body.toString(), headers, HttpStatus.CREATED)
				deferredResult.setResult(responseEntity)
			}
		}
		return deferredResult;
	}
}