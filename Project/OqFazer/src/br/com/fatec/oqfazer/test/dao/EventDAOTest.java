package br.com.fatec.oqfazer.test.dao;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.fatec.oqfazer.api.dao.CategoryDAO;
import br.com.fatec.oqfazer.api.dao.CityDAO;
import br.com.fatec.oqfazer.api.dao.EventCategory;
import br.com.fatec.oqfazer.api.dao.EventDAO;
import br.com.fatec.oqfazer.api.dao.Participation;
import br.com.fatec.oqfazer.api.dao.RegionDAO;
import br.com.fatec.oqfazer.api.dao.UserDAO;
import br.com.fatec.oqfazer.api.entity.Category;
import br.com.fatec.oqfazer.api.entity.City;

import br.com.fatec.oqfazer.api.entity.Event;
import br.com.fatec.oqfazer.api.entity.Region;
import br.com.fatec.oqfazer.api.entity.User;
import br.com.fatec.oqfazer.test.common.TestBase;
import br.com.spektro.minispring.core.implfinder.ImplFinder;

public class EventDAOTest extends TestBase {
	
	private EventDAO eventDAO;
	private UserDAO userDAO;
	private CategoryDAO categoryDAO;
	private RegionDAO regionDAO;
	private CityDAO cityDAO;
	private Participation participation;
	private EventCategory eventCategory;
	
	private User user1, user2, user3;
	private Category category1, category2;
	private Region region1, region2;
	
	@Before
	public void config() {
		this.eventDAO = ImplFinder.getImpl(EventDAO.class);
		this.userDAO = ImplFinder.getImpl(UserDAO.class);
		this.categoryDAO = ImplFinder.getImpl(CategoryDAO.class);
		this.regionDAO = ImplFinder.getImpl(RegionDAO.class);
		this.cityDAO = ImplFinder.getImpl(CityDAO.class);
		
		//USERS
		User user = new User();
		user.setId((long) 1);
		user.setName("User 1");
		user.setEmail("user1@test");
		user.setPassword("user1");
		user.setPhone(11111);
		
		User user2 = new User();
		user2.setId((long) 2);
		user2.setName("User 2");
		user2.setEmail("user2@test");
		user2.setPassword("user2");
		user2.setPhone(22222);
		
		User user3 = new User();
		user3.setId((long) 3);
		user3.setName("User 3");
		user3.setEmail("user3@test");
		user3.setPassword("user3");
		user3.setPhone(3333);
		
		//CATEGORIES
		Category category1 = new Category();
		category1.setId((long) 1);
		category1.setName("Show");
		category1.setCategory(null);
		
		Category category2 = new Category();
		category1.setId((long) 1);
		category2.setName("Show Rock");
		category2.setCategory(category1);
		
		//LIST DE CITIES AND REGIONS
		List<City> citiesRio = null;
		citiesRio.add(City.CACAPAVA);
		citiesRio.add(City.TAUBATE);		
		
		Region region1 = new Region();
		region1.setId((long) 1);
		region1.setName("Sentido Rio");
		
		List<City> citiesSaoPaulo = null;
		citiesSaoPaulo.add(City.SAO_JOSE_DOS_CAMPOS);
		citiesSaoPaulo.add(City.JACAREI);
		
		Region region2 = new Region();
		region1.setId((long) 1);
		region1.setName("Sentido Sao Paulo");
		
		//INSERTS
		Long idUser1 = this.userDAO.insertUser(user);
		Long idUser2 = this.userDAO.insertUser(user2);
		Long idUser3 = this.userDAO.insertUser(user3);
		Long idCategory = this.categoryDAO.insertCategory(category1);
		Long idCategory2 = this.categoryDAO.insertCategory(category2);
		Long idRegion1 = this.regionDAO.insertRegion(region1);
		Long idRegion2 = this.regionDAO.insertRegion(region2);
		this.cityDAO.insertCity(idRegion1, citiesRio);
		this.cityDAO.insertCity(idRegion2, citiesSaoPaulo);
		
		
				
		this.user1 = this.userDAO.searchUserById(idUser1);
		this.user2 = this.userDAO.searchUserById(idUser2);
		this.user3 = this.userDAO.searchUserById(idUser3);
		this.category1 = this.categoryDAO.searchCategoryById(idCategory);
		this.category2 = this.categoryDAO.searchCategoryById(idCategory2);
		this.region1 = this.regionDAO.searchRegionById(idRegion1);
		this.region2 = this.regionDAO.searchRegionById(idRegion2);
	}
	
	@Test
	public void testSave() {	
		Event event = new Event();
		event.setId((long) 1);
		event.setName("Show da Banda");
		event.setDescription("Show da banda fulana");
		event.setRegistration_date(new Date());
		event.setEvent_date(new Date(2016,03,30));
		event.setLocal("Rua jos�");
		event.setImageURL(null);
		event.setRegion(region1);
		event.setOwner(user1);
					
		Long id = this.eventDAO.inserEvent(event);
		
		List<User> users = null;
		users.add(user2);
		users.add(user3);
		
		List<Category> categories = null;
		categories.add(category1);
		categories.add(category2);
		
		this.participation.updateListParticipation(id, users);
		this.eventCategory.updateEventCategory(id, categories);
		
		Event savedEvent = this.eventDAO.searchEventById(id);
		
		Assert.assertNotNull(savedEvent);
		Assert.assertEquals(String.valueOf(1), savedEvent.getId());
		Assert.assertEquals("Show da Banda", savedEvent.getName());
		Assert.assertEquals("Show da banda fulana", savedEvent.getDescription());
		Assert.assertEquals(new Date(), savedEvent.getRegistration_date());
		Assert.assertEquals(new Date(2016,03,30), savedEvent.getEvent_date());
		Assert.assertEquals("Rua Jos�", savedEvent.getLocal());
		Assert.assertEquals(null, savedEvent.getImageURL());
		Assert.assertEquals(this.region1, savedEvent.getRegion());
		Assert.assertEquals(this.user1, savedEvent.getOwner());
	}
	
	@Test
	public void testUpdate() {
		Event event = new Event();
		event.setId((long) 1);
		event.setName("Show da Banda");
		event.setDescription("Show da banda fulana");
		event.setRegistration_date(new Date());
		event.setEvent_date(new Date(2016,03,30));
		event.setLocal("Rua jos�");
		event.setImageURL(null);
		event.setRegion(region1);
		event.setOwner(user1);
		
		Long id = this.eventDAO.inserEvent(event);
		
		List<User> users = null;
		users.add(user1);
		users.add(user3);
		
		List<Category> categories = null;
		categories.add(category1);
		categories.add(category2);
		
		this.participation.updateListParticipation(id, users);
		this.eventCategory.updateEventCategory(id, categories);
		
		Event updateEvent = this.eventDAO.searchEventById(id);		
		
		updateEvent.setName("Show da Banda de Musica");
		updateEvent.setDescription("Show da banda fulana de musica");
		updateEvent.setRegistration_date(new Date());
		updateEvent.setEvent_date(new Date(2017,03,20));
		updateEvent.setLocal("Rua joao");
		updateEvent.setImageURL(null);
		updateEvent.setRegion(region2);
		updateEvent.setOwner(user2);
		
		this.eventDAO.updateEvent(updateEvent);
		
		Assert.assertNotNull(updateEvent);
		Assert.assertEquals(String.valueOf(1), updateEvent.getId());
		Assert.assertEquals("Show da Banda de musica", updateEvent.getName());
		Assert.assertEquals("Show da banda fulana de musica", updateEvent.getDescription());
		Assert.assertEquals(new Date(), updateEvent.getRegistration_date());
		Assert.assertEquals(new Date(2017,03,20), updateEvent.getEvent_date());
		Assert.assertEquals("Rua joao", updateEvent.getLocal());
		Assert.assertEquals(null, updateEvent.getImageURL());
		Assert.assertEquals(this.region2, updateEvent.getRegion());
		Assert.assertEquals(this.user2, updateEvent.getOwner());		
	}
	
	@Test
	public void testDelete() {
		Event event = new Event();
		event.setId((long) 1);
		event.setName("Show da Banda");
		event.setDescription("Show da banda fulana");
		event.setRegistration_date(new Date());
		event.setEvent_date(new Date(2016,03,30));
		event.setLocal("Rua jos�");
		event.setImageURL(null);
		event.setRegion(region1);
		event.setOwner(user1);
		
		Long id = this.eventDAO.inserEvent(event);
			
		List<User> users = null;
		users.add(user2);
		users.add(user3);
		
		List<Category> categories = null;
		categories.add(category1);
		categories.add(category2);
		
		this.participation.updateListParticipation(id, users);
		this.eventCategory.updateEventCategory(id, categories);
		
		Event deletedEvent = this.eventDAO.searchEventById(id);
		
		Assert.assertNull(deletedEvent);
	}
	
	@Test
	public void testsearchAll() {
		Event event = new Event();
		event.setId((long) 1);
		event.setName("Show da Banda");
		event.setDescription("Show da banda fulana");
		event.setRegistration_date(new Date());
		event.setEvent_date(new Date(2016,03,30));
		event.setLocal("Rua jos�");
		event.setImageURL(null);
		event.setRegion(region1);
		event.setOwner(user1);
		
		Long idEvent = this.eventDAO.inserEvent(event);
		
		List<User> users = null;
		users.add(user2);
		users.add(user3);
		this.participation.updateListParticipation(idEvent, users);
		
		Event event2 = new Event();
		event2.setId((long) 1);
		event2.setName("Show da Banda2");
		event2.setDescription("Show da banda fulana2");
		event2.setRegistration_date(new Date());
		event2.setEvent_date(new Date(2017,04,10));
		event2.setLocal("Rua jo�o");
		event2.setImageURL(null);
		event2.setRegion(region2);
		event2.setOwner(user2);
		
		Long idEvent2 = this.eventDAO.inserEvent(event2);
		
		List<User> users2 = null;
		users2.add(user1);
		users2.add(user3);
		this.participation.updateListParticipation(idEvent2, users2);
		
		List<Event> listEvent = this.eventDAO.searchAllEvents();
		
		Assert.assertEquals(2, listEvent.size());
		Assert.assertEquals(String.valueOf(1), listEvent.get(0).getId());
		Assert.assertEquals("Show da Banda", listEvent.get(0).getName());
		Assert.assertEquals("Show da banda fulana", listEvent.get(0).getDescription());
		Assert.assertEquals(new Date(), listEvent.get(0).getRegistration_date());
		Assert.assertEquals(new Date(2016,03,30), listEvent.get(0).getEvent_date());
		Assert.assertEquals("Rua jos�", listEvent.get(0).getLocal());
		Assert.assertEquals(null, listEvent.get(0).getImageURL());
		Assert.assertEquals(this.region1, listEvent.get(0).getRegion());
		Assert.assertEquals(this.user1, listEvent.get(0).getOwner());
		
		Assert.assertEquals(String.valueOf(1), listEvent.get(1).getId());
		Assert.assertEquals("Show da Banda2", listEvent.get(1).getName());
		Assert.assertEquals("Show da banda fulana2", listEvent.get(1).getDescription());
		Assert.assertEquals(new Date(), listEvent.get(1).getRegistration_date());
		Assert.assertEquals(new Date(2017,04,10), listEvent.get(1).getEvent_date());
		Assert.assertEquals("Rua jo�o", listEvent.get(1).getLocal());
		Assert.assertEquals(null, listEvent.get(1).getImageURL());
		Assert.assertEquals(this.region2, listEvent.get(1).getRegion());
		Assert.assertEquals(this.user2, listEvent.get(1).getOwner());
	}
	
}
