package in.ashokit.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import in.ashokit.binding.CitizenApp;
import in.ashokit.entity.CitizenAppEntity;
import in.ashokit.repo.CitizenAppRepo;

@Service
public class ArServiceImpl implements ArService{

	@Autowired
	private CitizenAppRepo repo;
	
	@Override
	public Integer createAppliction(CitizenApp app) {
		
		String endPointUrl ="http://54.252.253.162:9090/state/{ssn}";
		//first way of calling one project api into another one 
		
//		RestTemplate rt = new RestTemplate();
//		ResponseEntity<String> responseEntity = rt.getForEntity(endPointUrl, String.class, app.getSsn());
//		String stateName = responseEntity.getBody();
		
		//second way of calling one project api into another one 
		WebClient webClient = WebClient.create();
		String stateName = webClient.get() //it represents get request
		                            .uri(endPointUrl, app.getSsn()) // it represets to url to send req
		                            .retrieve()     //to retrieve response
		                            .bodyToMono(String.class)   // specify response type 
		                            .block();   // to make synchronous call
		
		if("New Jersey".equals(stateName)) {
			
			CitizenAppEntity entity = new CitizenAppEntity();
			BeanUtils.copyProperties(app, entity);
			entity.setStateName(stateName);
			CitizenAppEntity save = repo.save(entity);
			
			
			return save.getAppId();
		}
		return 0;
	}

	
}
