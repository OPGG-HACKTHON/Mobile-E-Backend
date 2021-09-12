package opgg.backend.gmakersserver.domain.profile.service;

import static opgg.backend.gmakersserver.domain.profile.controller.response.ProfileResponse.Auth.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.merakianalytics.orianna.types.core.summoner.Summoner;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.application.util.DeduplicationUtils;
import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.account.repository.AccountRepository;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Queue;
import opgg.backend.gmakersserver.domain.preferkeyword.entity.Keyword;
import opgg.backend.gmakersserver.domain.profile.controller.request.ProfileRequest;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileDetailResponse;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileFindResponse;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileResponse;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;
import opgg.backend.gmakersserver.domain.profile.repository.ProfileRepository;
import opgg.backend.gmakersserver.error.exception.account.AccountNotFoundException;
import opgg.backend.gmakersserver.error.exception.preferchampion.PreferChampionBoundsException;
import opgg.backend.gmakersserver.error.exception.preferchampion.PreferChampionPriorityDuplicateException;
import opgg.backend.gmakersserver.error.exception.preferkeyword.PreferKeywordBoundsException;
import opgg.backend.gmakersserver.error.exception.preferkeyword.PreferKeywordDuplicateException;
import opgg.backend.gmakersserver.error.exception.preferline.PreferLineBoundsException;
import opgg.backend.gmakersserver.error.exception.preferline.PreferLinePriorityDuplicateException;
import opgg.backend.gmakersserver.error.exception.profile.ProfileBoundsException;
import opgg.backend.gmakersserver.error.exception.profile.ProfileExistException;
import opgg.backend.gmakersserver.error.exception.profile.ProfileNotExistException;
import opgg.backend.gmakersserver.error.exception.profile.ProfileNotMatchException;
import opgg.backend.gmakersserver.error.exception.riotapi.SummonerNotFoundException;
import opgg.backend.gmakersserver.error.exception.summoner.SummonerAlreadyCertifiedException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileService {

	private final ProfileRepository profileRepository;
	private final AccountRepository accountRepository;
	private final ProfileDomainService profileDomainService;

	private boolean isMoreThanTwoMinute(long betweenSecond, long twoMinuteSecond) {
		return betweenSecond > twoMinuteSecond;
	}

	private void validUpdateProfile(List<ProfileRequest.Create.PreferChampion> updatePreferChampions,
			List<ProfileRequest.Create.PreferLine> updatePreferLines, List<Keyword> updatePreferKeywords) {
		if (isNotCreatePreferChampions(updatePreferChampions)) {
			throw new PreferChampionBoundsException();
		}

		if (isNotCreatePreferLines(updatePreferLines)) {
			throw new PreferLineBoundsException();
		}

		if ((isNotCreatePreferKeywords(updatePreferKeywords))) {
			throw new PreferKeywordBoundsException();
		}
	}

	private void validAccount(Account account) {
		long profileCount = profileRepository.countByAccount(account);
		if (isNotCreateProfile(profileCount)) {
			throw new ProfileBoundsException();
		}
	}

	private void validCreateProfile(ProfileRequest.Create profileRequest, Summoner summoner) {
		if (ObjectUtils.isEmpty(summoner.getProfileIcon())) {
			throw new SummonerNotFoundException();
		}
		if (isNotCreatePreferChampions(profileRequest.getPreferChampions())) {
			throw new PreferChampionBoundsException();
		}
		if (isNotCreatePreferLines(profileRequest.getPreferLines())) {
			throw new PreferLineBoundsException();
		}
	}

	private List<ProfileFindResponse> getProfileFindResponses(String summonerName, Account account) {
		List<ProfileFindResponse> profileFindResponses;
		if (!StringUtils.isBlank(summonerName)) {
			profileFindResponses = profileRepository.findProfileBySummonerName(summonerName);
			if (CollectionUtils.isEmpty(profileFindResponses)) {
				Summoner summoner = Summoner.named(summonerName).get();
				summoner.load();
				if (ObjectUtils.isEmpty(summoner)) {
					throw new SummonerNotFoundException();
				}
				String findSummonerName = summoner.getCoreData().getName();
				profileFindResponses = profileRepository.findProfileBySummonerName(findSummonerName);
			}
		} else {
			profileFindResponses = profileRepository.findProfileMainByAccount(account);
		}
		return profileFindResponses.stream()
				.sorted(Comparator.comparingLong(ProfileFindResponse::getProfileId))
				.collect(Collectors.toList());
	}

	private boolean isNotCreatePreferKeywords(List<Keyword> updatePreferKeywords) {
		int size = updatePreferKeywords.size();
		if (size > 3) {
			return true;
		}
		int distinctSize = DeduplicationUtils.deduplication(updatePreferKeywords, Keyword::getKrKeyword).size();
		if (size != distinctSize) {
			throw new PreferKeywordDuplicateException();
		}
		return false;
	}

	private boolean isNotCreatePreferLines(List<ProfileRequest.Create.PreferLine> preferLines) {
		int size = preferLines.size();
		if (size > 2) {
			return true;
		}
		int distinctSize = DeduplicationUtils.deduplication(preferLines, ProfileRequest.Create.PreferLine::getPriority)
				.size();
		if (size != distinctSize) {
			throw new PreferLinePriorityDuplicateException();
		}
		return false;
	}

	private boolean isNotCreatePreferChampions(List<ProfileRequest.Create.PreferChampion> preferChampions) {
		int size = preferChampions.size();
		if (size > 3) {
			return true;
		}
		int distinctSize = DeduplicationUtils.deduplication(preferChampions,
				ProfileRequest.Create.PreferChampion::getPriority).size();
		if (size != distinctSize) {
			throw new PreferChampionPriorityDuplicateException();
		}
		return false;
	}

	private boolean isNotCreateProfile(long count) {
		return count >= 3;
	}


	private boolean isReliable(Profile profile) {
		Summoner summoner = Summoner.withId(profile.getSummonerInfo().getSummonerId()).get();
		int summonerProfileIconId = summoner.getProfileIcon().getId();
		if (profile.isAuthorizable(summonerProfileIconId)) {
			profile.changeIsCertified(true);
			profile.changeAuthProfileIconId(-1);
			return true;
		}
		return false;
	}

	private boolean isAuthProfile(Profile profile) {
		return !profile.isCertified() && !isReliable(profile);
	}

	@Transactional
	public void createProfile(ProfileRequest.Create profileRequest, Long id) {
		Summoner summoner = Summoner.named(profileRequest.getSummonerName()).get();
		summoner.load();
		validCreateProfile(profileRequest, summoner);
		Account account = accountRepository.findByAccountId(id).orElseThrow(AccountNotFoundException::new);
		validAccount(account);
		profileRepository.findByAccountAndSummonerName(account.getAccountId(), summoner.getName())
				.ifPresent(profile -> {
					throw new ProfileExistException();
				});

		Profile profile = Profile.of(account, summoner, profileRequest);
		profileRepository.save(profile);
		profileDomainService.createProfileDomain(profileRequest, summoner, profile);
		profile.changePreferQueue(profileDomainService.getPreferQueue(profileRequest.getPreferQueue(), profile));
	}

	@Transactional
	public ProfileResponse.Auth authProfile(ProfileRequest.Auth auth, Long id) {
		String summonerId = auth.getSummonerId();
		Summoner summoner = Summoner.withId(summonerId).get();
		summoner.load();

		profileRepository.findProfileBySummonerName(summoner.getName()).stream()
				.filter(ProfileFindResponse::isCertified)
				.findFirst()
				.ifPresent(profileFindResponse -> {
					throw new SummonerAlreadyCertifiedException();
				});

		Profile profile = profileRepository.findBySummonerIdAndAccountId(summonerId, id)
				.orElseThrow(SummonerNotFoundException::new);
		int iconId = -1;
		if (isAuthProfile(profile)) {
			iconId = profile.getRandomIconId();
			profile.changeAuthProfileIconId(iconId);
		}
		return from(iconId);
	}

	@Transactional
	public ProfileResponse.AuthConfirm authConfirm(ProfileRequest.Auth auth, Long id) {
		return ProfileResponse.AuthConfirm.from(profileRepository.findBySummonerIdAndAccountId(auth.getSummonerId(), id)
				.orElseThrow(SummonerNotFoundException::new).getAuthConfirm());
	}

	@Transactional
	public List<ProfileFindResponse> getProfiles(String summonerName, Long id) {
		Account account = accountRepository.findByAccountId(id).orElseThrow(AccountNotFoundException::new);
		Profile profile = profileRepository.findById(id).orElseThrow(ProfileNotExistException::new);
		profile.getLeaguePositions()
				.forEach(leaguePosition -> refreshProfile(profile));
		return new ProfileFindResponse().convert(
				getProfileFindResponses(summonerName, account));
	}

	private void refreshProfile(Profile profile) {
		LocalDateTime lastModifiedDate = profile.getLastModifiedDate();
		LocalDateTime now = LocalDateTime.now();
		long betweenSecond = Duration.between(lastModifiedDate, now).getSeconds();
		long twoMinuteSecond = Duration.ofMinutes(2L).getSeconds();

		if (isMoreThanTwoMinute(betweenSecond, twoMinuteSecond)) {
			Summoner summoner = Summoner.withAccountId(profile.getSummonerAccountId()).get();
			summoner.load();

			int summonerProfileIconId = summoner.getProfileIcon().getId();
			int profileIconId = profile.getSummonerInfo().getProfileIconId();
			if (summonerProfileIconId != profileIconId) {
				profile.changeProfileIconId(summonerProfileIconId);
			}
			profileDomainService.updateLeaguePosition(summoner, profile);
		}

	}

	@Transactional
	public ProfileDetailResponse getProfile(Long profileId, Long id) {
		Account account = accountRepository.findByAccountId(id).orElseThrow(AccountNotFoundException::new);
		Profile profile = profileRepository.findById(profileId).orElseThrow(ProfileNotExistException::new);
		profile.getLeaguePositions()
				.forEach(leaguePosition -> refreshProfile(profile));
		List<ProfileDetailResponse> profileDetailResponses = profileRepository.findProfileDetailByAccountAndProfile(
				account, profile);
		return new ProfileDetailResponse(profileDetailResponses);
	}

	@Transactional
	public void deleteProfile(Long profileId, Long id) {
		Account account = accountRepository.findByAccountId(id).orElseThrow(AccountNotFoundException::new);
		Profile profile = profileRepository.findByAccountAndProfileId(account, profileId).orElseThrow(
				ProfileNotMatchException::new);
		profileRepository.delete(profile);
	}

	@Transactional
	public void updateProfile(Long profileId, ProfileRequest.Update update, Long id) {
		Account account = accountRepository.findByAccountId(id).orElseThrow(AccountNotFoundException::new);
		Profile profile = profileRepository.findByAccountAndProfileId(account, profileId).orElseThrow(
				ProfileNotMatchException::new);

		List<ProfileRequest.Create.PreferChampion> updatePreferChampions = update.getPreferChampions();
		List<ProfileRequest.Create.PreferLine> updatePreferLines = update.getPreferLines();
		List<Keyword> updatePreferKeywords = update.getPreferKeywords();

		validUpdateProfile(updatePreferChampions, updatePreferLines, updatePreferKeywords);
		profileDomainService.profileDomainUpdate(update, profile, updatePreferChampions,
				updatePreferLines, updatePreferKeywords);

		String description = update.getDescription();
		if (!StringUtils.isBlank(description)) {
			profile.changeDescription(description);
		}

		Queue preferQueue = update.getPreferQueue();
		if (!ObjectUtils.isEmpty(preferQueue)) {
			profile.changePreferQueue(profileDomainService.getPreferQueue(preferQueue, profile));
		}

	}

}