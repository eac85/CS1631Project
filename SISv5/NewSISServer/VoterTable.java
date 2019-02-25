import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class VoterTable {
	
	private ArrayList<String> alreadyVoted = new ArrayList<String>();
	private HashMap<String, Integer> candidates = new HashMap<String, Integer>();

	public void addCandidate(String candidate){
		candidates.put(candidate, 0);
	}

	public boolean searchCandidates(String candidateId){
		if(candidates.isEmpty())
			return false;
		else
			return candidates.containsKey(candidateId);
	}

	public boolean checkVoter(String phone){
		return alreadyVoted.contains(phone);
	}

	public void addVoter(String phone){
		alreadyVoted.add(phone);
	}

	public void castVote(String voterPhone, String candidateId){
		if(!checkVoter(voterPhone)){
			if(searchCandidates(candidateId)){
				System.out.println("Casting for exisiting candidate");
				candidates.put(candidateId, candidates.get(candidateId) + 1);
			}
			else {
				System.out.println("Casting and Adding Candidate");
				candidates.put(candidateId, 1);
			}
			addVoter(voterPhone);
		}
		else{
			System.out.println("User Already Voted");
		}
	}

	public HashMap<String,Integer> getResults(){
		return candidates;
	}


}