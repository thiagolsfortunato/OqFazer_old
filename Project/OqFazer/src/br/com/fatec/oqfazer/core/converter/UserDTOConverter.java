package br.com.fatec.oqfazer.core.converter;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import br.com.fatec.oqfazer.api.dao.EventDAO;
import br.com.fatec.oqfazer.api.dao.Participation;
import br.com.fatec.oqfazer.api.dao.UserDAO;
import br.com.fatec.oqfazer.api.dto.EventDTO;
import br.com.fatec.oqfazer.api.dto.UserDTO;
import br.com.fatec.oqfazer.api.entity.Event;
import br.com.fatec.oqfazer.api.entity.User;
import br.com.spektro.minispring.core.implfinder.ImplFinder;
import br.com.spektro.minispring.dto.DTOConverter;

public class UserDTOConverter implements DTOConverter<User, UserDTO>{
	
	private EventDAO eventDao;
	private Participation participationDao;
	private EventDTOConverter eventConverter;
	
	public UserDTOConverter() {
		ImplFinder.getImpl(UserDAO.class);
		this.eventDao = ImplFinder.getImpl(EventDAO.class);
		this.participationDao = ImplFinder.getImpl(Participation.class);
	}
	
	@Override
	public UserDTO toDTO(User entityUser) {
		return this.toDTO(entityUser);
	}
	
	public UserDTO toDTO(User entityUser, boolean convertDependences){
		UserDTO dtoUser = this.toDTOSimple(entityUser);
		Long id = entityUser.getId();
		if(id!=null && convertDependences){
			List<Long> idsEvents = this.participationDao.searchEvents(id);
			List<Event> entityEvents = this.eventDao.searchEventsByListIds(idsEvents);
			List<EventDTO> eventsDTO = this.eventConverter.toDTO(entityEvents);
			Set<EventDTO> eventsUsers = Sets.newLinkedHashSet();
			eventsUsers.addAll(eventsDTO);
			dtoUser.setEventos(eventsDTO);
			dtoUser.setEventsUsers(eventsUsers);
			
			dtoUser.setIsOwner(dtoUser.isOwner("OWNER"));
		}
		return dtoUser;
	}
	
	public UserDTO toDTOSimple (User entityUser){
		UserDTO dto = new UserDTO();
		dto.setId(entityUser.getId());
		dto.setName(entityUser.getName());
		dto.setEmail(entityUser.getEmail());
		dto.setPassword(entityUser.getPassword());
		dto.setPhone(entityUser.getPhone());
		return dto;
	}
	
	@Override
	public List<UserDTO> toDTO(List<User> users) {
		return this.toDTO(users, false);
	}
	
	public List<UserDTO> toDTOSimple(List<User> users) {
		return this.toDTO(users, true);
	}
	
	@Override
	public User toEntity(UserDTO dto) {
		User entity = new User();
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity.setEmail(dto.getEmail());
		entity.setPassword(dto.getPassword());
		entity.setPhone(dto.getPhone());
		return entity;
	}
	@Override
	public List<User> toEntity(List<UserDTO> dtos) {
		List<User> entities = Lists.newArrayList();
		for(UserDTO dto: dtos){
			entities.add(this.toEntity(dto));
		}
		return entities;
	}
	
	private List<UserDTO> toDTO (List<User> entities, boolean isSimple){
		List<UserDTO> dtos = Lists.newArrayList();
		for (User entity: entities){
			dtos.add(this.toDTO(entity, isSimple));
		}
		return dtos;
	}

}
