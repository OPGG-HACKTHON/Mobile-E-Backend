package opgg.backend.gmakersserver.domain.profile.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.merakianalytics.orianna.types.core.summoner.Summoner;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.domain.leagueposition.entity.Queue;
import opgg.backend.gmakersserver.domain.leagueposition.service.LeaguePositionService;
import opgg.backend.gmakersserver.domain.preferkeyword.entity.Keyword;
import opgg.backend.gmakersserver.domain.preferkeyword.service.PreferKeywordService;
import opgg.backend.gmakersserver.domain.preferchampion.service.PreferChampionService;
import opgg.backend.gmakersserver.domain.preferline.service.PreferLineService;
import opgg.backend.gmakersserver.domain.profile.controller.request.ProfileRequest;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileDomainService {

	private final LeaguePositionService leaguePositionService;
	private final PreferLineService preferLineService;
	private final PreferChampionService preferChampionService;
	private final PreferKeywordService preferKeywordService;

	@Transactional
	public void createProfileDomain(ProfileRequest.Create profileRequest, Summoner summoner, Profile profile) {
		preferChampionService.createPreferChampion(profileRequest.getPreferChampions(),
				profileRequest.getSummonerName(), profile);
		preferLineService.createPreferLine(profileRequest.getPreferLines(), profile);
		leaguePositionService.createLeaguePosition(summoner, profile);
		preferKeywordService.createPreferKeyword(profileRequest.getPreferKeywords(), profile);
	}

	@Transactional
	public void updateLeaguePosition(Summoner summoner, Profile profile) {
		leaguePositionService.updateLeaguePosition(summoner, profile);
	}

	public Queue getPreferQueue(Queue preferQueue, Profile profile) {
		return leaguePositionService.getPreferQueue(preferQueue, profile);
	}

	@Transactional
	public void updatePreferLine(List<ProfileRequest.Create.PreferLine> preferLines, Profile profile) {
		preferLineService.updatePreferLine(preferLines, profile);
	}

	@Transactional
	public void updatePreferChampion(List<ProfileRequest.Create.PreferChampion> updatePreferChampions,
			String summonerName, Profile profile) {
		preferChampionService.updatePreferChampion(updatePreferChampions, summonerName, profile);
	}

	@Transactional
	public void updatePreferKeyword(List<Keyword> keywords, Profile profile) {
		preferKeywordService.updatePreferKeyword(keywords, profile);
	}

	@Transactional
	public void profileDomainUpdate(ProfileRequest.Update update, Profile profile,
			List<ProfileRequest.Create.PreferChampion> updatePreferChampions,
			List<ProfileRequest.Create.PreferLine> updatePreferLines, List<Keyword> updatePreferKeywords) {
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

	}

}
