package guru.qa.niffler.service;

import guru.qa.niffler.data.CurrencyValues;
import guru.qa.niffler.data.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.ex.NotFoundException;
import guru.qa.niffler.model.FriendJson;
import guru.qa.niffler.model.UserJson;
import niffler_userdata.FriendState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static guru.qa.niffler.model.FriendState.*;
import static guru.qa.niffler.model.UserJson.fromEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDataServiceTest {

	private UserDataService testedObject;
	private final UUID mainTestUuid = UUID.randomUUID();
	private final String mainTestUsername = "ivanov";
	private UserEntity mainTestUser;

	private final UUID secondTestUuid = UUID.randomUUID();
	private final String secondTestUsername = "Adam 5";
	private UserEntity secondTestUser;

	private final UUID thirdTestUuid = UUID.randomUUID();
	private final String thirdTestUsername = "Adam 55";
	private UserEntity thirdTestUser;

	private final String nonExistentUsername = "non_existent";
	private FriendJson friendJson;


	@BeforeEach
	void init() {
		mainTestUser = new UserEntity();
		mainTestUser.setId(mainTestUuid);
		mainTestUser.setUsername(mainTestUsername);
		mainTestUser.setCurrency(CurrencyValues.RUB);

		secondTestUser = new UserEntity();
		secondTestUser.setId(secondTestUuid);
		secondTestUser.setUsername(secondTestUsername);
		secondTestUser.setCurrency(CurrencyValues.RUB);

		thirdTestUser = new UserEntity();
		thirdTestUser.setId(thirdTestUuid);
		thirdTestUser.setUsername(thirdTestUsername);
		thirdTestUser.setCurrency(CurrencyValues.RUB);
	}

	@ValueSource(strings = {"photo", ""})
	@ParameterizedTest
	void userShouldBeUpdated(String photo, @Mock UserRepository userRepository) {
		lenient().when(userRepository.findByUsername(eq(mainTestUsername)))
				.thenReturn(mainTestUser);
		lenient().when(userRepository.save(any(UserEntity.class)))
				.thenAnswer(answer -> answer.getArguments()[0]);
		testedObject = new UserDataService(userRepository);


		final String photoForTest = photo.equals("") ? null : photo;
		final UserJson toBeUpdated = new UserJson();
		toBeUpdated.setUsername(mainTestUsername);
		toBeUpdated.setFirstname("Test");
		toBeUpdated.setSurname("TestSurname");
		toBeUpdated.setCurrency(CurrencyValues.USD);
		toBeUpdated.setPhoto(photoForTest);
		final UserJson result = testedObject.update(toBeUpdated);
		assertEquals(mainTestUuid, result.getId());
		assertEquals(mainTestUsername, result.getUsername());
		assertEquals("Test", result.getFirstname());
		assertEquals("TestSurname", result.getSurname());
		assertEquals(CurrencyValues.USD, result.getCurrency());
		assertEquals(photoForTest, result.getPhoto());

		verify(userRepository, times(1)).save(any(UserEntity.class));
	}

	@Test
	void getRequiredUserShouldThrowNotFoundExceptionIfIfUserNotFound(@Mock UserRepository userRepository) {
		lenient().when(userRepository.findByUsername(eq(nonExistentUsername)))
				.thenReturn(null);

		testedObject = new UserDataService(userRepository);

		final NotFoundException exception = assertThrows(NotFoundException.class,
				() -> testedObject.getRequiredUser(nonExistentUsername));
		assertEquals("Can`t find user by username: " + nonExistentUsername,
				exception.getMessage());
	}

	@Test
	void allUserShouldReturnCorrectUsersList(@Mock UserRepository userRepository) {
		when(userRepository.findByUsernameNot(eq(mainTestUsername)))
				.thenReturn(getMockUsersMappingFromDb());
		testedObject = new UserDataService(userRepository);
		final List<UserJson> users = testedObject.allUsers(mainTestUsername);
		assertEquals(2, users.size());
		final UserJson invitation = users.stream()
				.filter(u -> u.getFriendState() == INVITE_SENT)
				.findFirst()
				.orElseThrow(() -> new AssertionError("Friends with state INVITE_SENT not found"));

		final UserJson friend = users.stream()
				.filter(u -> u.getFriendState() == FRIEND)
				.findFirst()
				.orElseThrow(() -> new AssertionError("Friends with state FRIEND not found"));

		assertEquals(secondTestUsername, invitation.getUsername());
		assertEquals(thirdTestUsername, friend.getUsername());
	}

	static Stream<Arguments> friendsShouldReturnDifferentListBasedOnIncludePendingParam() {
		return Stream.of(
				Arguments.of(true, List.of(INVITE_SENT, FRIEND)),
				Arguments.of(false, List.of(FRIEND))
		);
	}

	static Stream<Arguments> invitationsShouldReturnInvitationReceived() {
		return Stream.of(
				Arguments.of(List.of(INVITE_RECEIVED)));
	}

	@MethodSource
	@ParameterizedTest
	void friendsShouldReturnDifferentListBasedOnIncludePendingParam(boolean includePending,
	                                                                List<FriendState> expectedState,
	                                                                @Mock UserRepository userRepository) {
		when(userRepository.findByUsername(eq(mainTestUsername)))
				.thenReturn(enrichTestUser());

		testedObject = new UserDataService(userRepository);
		final List<UserJson> result = testedObject.friends(mainTestUsername, includePending);
		assertEquals(expectedState.size(), result.size());

		assertTrue(result.stream()
				.map(UserJson::getFriendState)
				.toList()
				.containsAll(expectedState));
	}

	@MethodSource
	@ParameterizedTest
	void invitationsShouldReturnInvitationReceived(List<FriendState> expectedState, @Mock UserRepository userRepository){
		when(userRepository.findByUsername(eq(mainTestUsername)))
				.thenReturn(invitesTestUser());
		testedObject = new UserDataService(userRepository);

		final List<UserJson> result = testedObject.invitations(mainTestUsername);
		assertEquals(expectedState.size(), result.size());
		assertTrue(result.stream()
				.map(UserJson::getFriendState)
				.toList()
				.containsAll(expectedState));
	}

	@Test
	void addFriendsShouldReturnInviteSent(@Mock UserRepository userRepository){
		friendJson = new FriendJson();
		friendJson.setUsername(secondTestUsername);

		when(userRepository.findByUsername(eq(mainTestUsername)))
				.thenReturn(mainTestUser);
		when(userRepository.findByUsername(eq(secondTestUsername)))
				.thenReturn(secondTestUser);
		testedObject = new UserDataService(userRepository);
		UserJson result = testedObject.addFriend(mainTestUsername, friendJson);
		assertEquals(INVITE_SENT, result.getFriendState());
		assertEquals(secondTestUsername, result.getUsername());
	}


	@Test
	void acceptInvitationShouldReturnAcceptedInvitation(@Mock UserRepository userRepository){
		friendJson = new FriendJson();
		friendJson.setUsername(secondTestUsername);

		when(userRepository.findByUsername(eq(mainTestUsername)))
				.thenReturn(mainTestUser);
		when(userRepository.findByUsername(eq(secondTestUsername)))
				.thenReturn(secondTestUser);
		mainTestUser.addInvites(secondTestUser);
		testedObject = new UserDataService(userRepository);

		List<UserJson> result = testedObject.acceptInvitation(mainTestUsername, friendJson);
		assertEquals(1, result.size());
		assertEquals(FRIEND, result.get(0).getFriendState());
	}

	@Test
	void declineInvitationShouldReturnInviteReceived(@Mock UserRepository userRepository){
		friendJson = new FriendJson();
		friendJson.setUsername(secondTestUsername);

		when(userRepository.findByUsername(eq(mainTestUsername)))
				.thenReturn(mainTestUser);
		when(userRepository.findByUsername(eq(secondTestUsername)))
				.thenReturn(secondTestUser);
		mainTestUser.addInvites(secondTestUser);
		mainTestUser.addInvites(thirdTestUser);
		testedObject = new UserDataService(userRepository);

		List<UserJson> result = testedObject.declineInvitation(mainTestUsername, friendJson);
		assertEquals(1, result.size());
		assertFalse(result.contains(fromEntity(secondTestUser)));
		assertEquals(thirdTestUsername, result.get(0).getUsername());
	}

	@Test
	void removeFriendShouldReturnRemovedFriendsWith(@Mock UserRepository userRepository){
		when(userRepository.findByUsername(eq(mainTestUsername)))
				.thenReturn(addFriendTestUser());
		when(userRepository.findByUsername(eq(secondTestUsername)))
				.thenReturn(secondTestUser);
		testedObject = new UserDataService(userRepository);

		List<UserJson> result = testedObject.removeFriend(mainTestUsername, secondTestUsername);
		assertFalse(result.contains(fromEntity(secondTestUser)));
	}

	private UserEntity enrichTestUser() {
		mainTestUser.addFriends(true, secondTestUser);
		secondTestUser.addInvites(mainTestUser);

		mainTestUser.addFriends(false, thirdTestUser);
		thirdTestUser.addFriends(false, mainTestUser);

		return mainTestUser;
	}

	private UserEntity addFriendTestUser() {
		mainTestUser.addFriends(false, secondTestUser);
		thirdTestUser.addFriends(false, mainTestUser);

		return mainTestUser;
	}

	private UserEntity invitesTestUser() {
		mainTestUser.addInvites(secondTestUser);
		secondTestUser.addFriends(true, mainTestUser);
		return mainTestUser;
	}

	private List<UserEntity> getMockUsersMappingFromDb() {
		mainTestUser.addFriends(true, secondTestUser);
		secondTestUser.addInvites(mainTestUser);

		mainTestUser.addFriends(false, thirdTestUser);
		thirdTestUser.addFriends(false, mainTestUser);

		return List.of(secondTestUser, thirdTestUser);
	}

}