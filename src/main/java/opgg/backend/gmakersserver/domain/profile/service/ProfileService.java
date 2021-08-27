package opgg.backend.gmakersserver.domain.profile.service;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
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
import opgg.backend.gmakersserver.domain.leagueposition.service.LeaguePositionService;
import opgg.backend.gmakersserver.domain.preferKeyword.entity.Keyword;
import opgg.backend.gmakersserver.domain.preferKeyword.service.PreferKeywordService;
import opgg.backend.gmakersserver.domain.preferchampion.service.PreferChampionService;
import opgg.backend.gmakersserver.domain.preferline.service.PreferLineService;
import opgg.backend.gmakersserver.domain.profile.controller.request.ProfileRequest;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileDetailResponse;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileFindResponse;
import opgg.backend.gmakersserver.domain.profile.controller.response.ProfileResponse;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;
import opgg.backend.gmakersserver.domain.profile.entity.SummonerInfo;
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
	private final LeaguePositionService leaguePositionService;
	private final PreferLineService preferLineService;
	private final PreferChampionService preferChampionService;
	private final PreferKeywordService preferKeywordService;

	private List<ProfileFindResponse> getProfileFindResponses(String summonerName, Account account) {
		List<ProfileFindResponse> profileMainByAccount;
		if (!StringUtils.isBlank(summonerName)) {
			profileMainByAccount = profileRepository.findProfileBySummonerName(summonerName);
			if (CollectionUtils.isEmpty(profileMainByAccount)) {
				Summoner summoner = Summoner.named(summonerName).get();
				summoner.load();
				if (ObjectUtils.isEmpty(summoner)) {
					throw new SummonerNotFoundException();
				}
				String findSummonerName = summoner.getCoreData().getName();
				profileMainByAccount = profileRepository.findProfileBySummonerName(findSummonerName);
			}
		} else {
			profileMainByAccount = profileRepository.findProfileMainByAccount(account);
		}
		return profileMainByAccount.stream()
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

	private int getRandomIconId(int profileIconId) {
		Random random = new Random();
		random.setSeed(System.currentTimeMillis());
		int iconId = profileIconId;
		while (iconId == profileIconId) {
			iconId = random.nextInt(28);
		}
		return iconId;
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

	private boolean isAuthConfirm(Profile profile) {
		return profile.isCertified() || isReliable(profile);
	}

	@Transactional
	public void createProfile(ProfileRequest.Create profileRequest, Long id) {
		String summonerName = profileRequest.getSummonerName();
		Summoner summoner = Summoner.named(summonerName).get();
		summoner.load();
		if (ObjectUtils.isEmpty(summoner.getProfileIcon())) {
			throw new SummonerNotFoundException();
		}

		if (isNotCreatePreferChampions(profileRequest.getPreferChampions())) {
			throw new PreferChampionBoundsException();
		}

		if (isNotCreatePreferLines(profileRequest.getPreferLines())) {
			throw new PreferLineBoundsException();
		}

		Account account = accountRepository.findByAccountId(id).orElseThrow(AccountNotFoundException::new);

		long profileCount = profileRepository.countByAccount(account);
		if (isNotCreateProfile(profileCount)) {
			throw new ProfileBoundsException();
		}

		Profile findProfile = profileRepository.findByAccountAndSummonerName(account.getAccountId(),
				summoner.getName());

		if (!ObjectUtils.isEmpty(findProfile)) {
			throw new ProfileExistException();
		}

		SummonerInfo summonerInfo = SummonerInfo.builder()
				.summonerId(summoner.getId())
				.summonerName(summoner.getName())
				.profileIconId(summoner.getProfileIcon().getId())
				.build();

		Profile profile = Profile.builder()
				.account(account)
				.isCertified(false)
				.authProfileIconId(null)
				.summonerAccountId(summoner.getAccountId())
				.summonerInfo(summonerInfo)
				.description(profileRequest.getDescription())
				.build();

		profileRepository.save(profile);

		preferChampionService.createPreferChampion(profileRequest.getPreferChampions(),
				profileRequest.getSummonerName(), profile);
		preferLineService.createPreferLine(profileRequest.getPreferLines(), profile);
		leaguePositionService.createLeaguePosition(summoner, profile);
		preferKeywordService.createPreferKeyword(profileRequest.getPreferKeywords(), profile);

		Queue queue = leaguePositionService.getPreferQueue(profileRequest, profile);
		profile.changePreferQueue(queue);
	}

	@Transactional
	public ProfileResponse.Auth authProfile(ProfileRequest.Auth auth, Long id) {
		String summonerId = auth.getSummonerId();
		Summoner summoner = Summoner.withId(summonerId).get();
		summoner.load();
		List<ProfileFindResponse> profileBySummonerNames = profileRepository.findProfileBySummonerName(
				summoner.getName());
		if (!CollectionUtils.isEmpty(profileBySummonerNames)) {
			profileBySummonerNames.stream()
					.filter(ProfileFindResponse::isCertified)
					.findFirst()
					.ifPresent(profileFindResponse -> {
						throw new SummonerAlreadyCertifiedException();
					});
		}
		Profile profile = profileRepository.findBySummonerIdAndAccountId(summonerId, id)
				.orElseThrow(SummonerNotFoundException::new);
		int iconId = -1;
		if (isAuthProfile(profile)) {
			iconId = getRandomIconId(profile.getSummonerInfo().getProfileIconId());
			profile.changeAuthProfileIconId(iconId);
		}
		return new ProfileResponse.Auth(iconId);
	}

	@Transactional
	public ProfileResponse.AuthConfirm authConfirm(ProfileRequest.Auth auth, Long id) {
		String summonerId = auth.getSummonerId();
		Profile profile = profileRepository.findBySummonerIdAndAccountId(summonerId, id)
				.orElseThrow(SummonerNotFoundException::new);
		return new ProfileResponse.AuthConfirm(isAuthConfirm(profile));
	}

	public List<ProfileFindResponse> getProfiles(String summonerName, Long id) {
		Account account = accountRepository.findByAccountId(id).orElseThrow(AccountNotFoundException::new);
		List<ProfileFindResponse> profileMainByAccount = getProfileFindResponses(summonerName, account);
		return new ProfileFindResponse().convert(profileMainByAccount);
	}

	public ProfileDetailResponse getProfile(Long profileId, Long id) {
		Account account = accountRepository.findByAccountId(id).orElseThrow(AccountNotFoundException::new);
		Profile profile = profileRepository.findById(profileId).orElseThrow(ProfileNotExistException::new);
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

		if (isNotCreatePreferChampions(updatePreferChampions)) {
			throw new PreferChampionBoundsException();
		}

		if (isNotCreatePreferLines(updatePreferLines)) {
			throw new PreferLineBoundsException();
		}

		if ((isNotCreatePreferKeywords(updatePreferKeywords))) {
			throw new PreferKeywordBoundsException();
		}

		if (!CollectionUtils.isEmpty(updatePreferChampions)) {
			preferChampionService.updatePreferChampion(update.getPreferChampions(),
					update.getSummonerName(), profile);
		}

		if (!CollectionUtils.isEmpty(updatePreferLines)) {
			preferLineService.updatePreferLine(updatePreferLines, profile);
		}

		if (!CollectionUtils.isEmpty(updatePreferKeywords)) {
			preferKeywordService.updatePreferKeyword(updatePreferKeywords, profile);
		}

		String description = update.getDescription();
		if (!StringUtils.isBlank(description)) {
			profile.changeDescription(description);
		}

		Queue preferQueue = update.getPreferQueue();
		if (!ObjectUtils.isEmpty(preferQueue)) {
			profile.changePreferQueue(preferQueue);
		}

	}

}