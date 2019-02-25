import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;

public class SISHandlers {
	// broadcast (up/down) and receiver
	// all "RECEIVER"s in the broadcast hierarchy can receive (scope startsWith)
	// ("A" in 1.1.1, "A" in 1.1, "A" in 1)

	// not broadcast and receiver
	// Only "RECEIVER" in specified SCOPE can receive (scope equals)
	// "A" in SCOPE

	// broadcast (up/down) and not receiver
	// all components in the hierarchy (up/down) (scope startsWith)
	// COMP1...COMPN in 1.1.1, COMP1...COMPN in 1.1, COMP1...COMPN in 1

	// not broadcast and not receiver
	// All components in specified SCOPE can receive (scope equals)
	// COMP1...COMPN in SCOPE

	static void ReadingHandler(String scope, String sender, String receiver,
			String direction, String broadcast, KeyValueList kvList) {

		KeyValueList conn = new KeyValueList();
			conn.putPair("MessageType", "Reader");
			conn.putPair("Purpose", "Reading");
			conn.putPair("Vote", "Read");

			SISServer.mapping.entrySet().stream()
								.filter(x -> (scope.startsWith(x.getKey().scope)
										&& (x.getKey().name.equals(receiver)
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(conn);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});

		if (sender != null && !sender.equals("")) {
			// a sender is required


			if (receiver != null) {
				if (broadcast != null && broadcast.equals("True")) {

					/*
					 * all "RECEIVER" in the broadcast hierarchy can receive
					 * (scope startsWith)
					 */
					if (direction != null && direction.equals("Up")) {

						SISServer.mapping.entrySet().stream()
								.filter(x -> (scope.startsWith(x.getKey().scope)
										&& (x.getKey().name.equals(receiver)
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});

					} else if (direction != null && direction.equals("Down")) {

						SISServer.mapping.entrySet().stream()
								.filter(x -> (x.getKey().scope.startsWith(scope)
										&& (x.getKey().name.equals(receiver)
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					}

				} else {
					/*
					 * Only "RECEIVER" in specified scope can receive
					 * (scope equals)
					 */
					SISServer.mapping.entrySet().stream()
							.filter(x -> (x.getKey().scope.equals(scope)
									&& (x.getKey().name.equals(receiver)
											|| x.getKey().componentType == ComponentType.Debugger)
							&& x.getValue().encoder != null)).forEach(x -> {
								try {
									// re-route this message to each
									// qualified
									// component
									x.getValue().encoder.sendMsg(kvList);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									System.out.println("ERROR: Fail to send "
											+ kvList + ", abort subtask");
								}
							});
				}

			} else {
				if (broadcast != null && broadcast.equals("True")) {
					/*
					 * all components in the hierarchy (up/down)
					 * (scope startsWith)
					 */
					if (direction != null && direction.equals("Up")) {
						SISServer.mapping.entrySet().stream()
								.filter(x -> (scope.startsWith(x.getKey().scope)
										&& (x.getKey().componentType == ComponentType.Monitor
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					} else if (direction != null && direction.equals("Down")) {
						SISServer.mapping.entrySet().stream()
								.filter(x -> (x.getKey().scope.startsWith(scope)
										&& (x.getKey().componentType == ComponentType.Monitor
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					}
				} else {
					/*
					 * All components in specified scope can receive
					 * (scope equals)
					 */
					SISServer.mapping.entrySet().stream()
							.filter(x -> (x.getKey().scope.equals(scope)
									&& (x.getKey().componentType == ComponentType.Monitor
											|| x.getKey().componentType == ComponentType.Debugger)
							&& x.getValue().encoder != null)).forEach(x -> {
								try {
									// re-route this message to each
									// qualified
									// component
									x.getValue().encoder.sendMsg(kvList);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									System.out.println("ERROR: Fail to send "
											+ kvList + ", abort subtask");
								}
							});
				}
			}
		}

		// no sender no distribution
	}

	static void AlertHandler(String scope, String sender, String receiver,
			String direction, String broadcast, KeyValueList kvList) {

		System.out.println("===========Handling Alert================");
		KeyValueList conn = new KeyValueList();
		conn.putPair("MessageType", "Alert");
		conn.putPair("Purpose", "PNMLAlert");
		conn.putPair("Vote", "NotCast");

		SISServer.mapping.entrySet().stream()
								.filter(x -> (scope.startsWith(x.getKey().scope)
										&& (x.getKey().name.equals(receiver)
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(conn);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});

		if (sender != null && !sender.equals("")) {
			// a sender is required
			System.out.println("Sender: "+sender);
			if (receiver != null) {
				if (broadcast != null && broadcast.equals("True")) {

					/*
					 * all "RECEIVER" in the broadcast hierarchy can receive
					 * (scope startsWith)
					 */
					if (direction != null && direction.equals("Up")) {

						SISServer.mapping.entrySet().stream()
								.filter(x -> (scope.startsWith(x.getKey().scope)
										&& (x.getKey().name.equals(receiver)
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});

					} else if (direction != null && direction.equals("Down")) {

						SISServer.mapping.entrySet().stream()
								.filter(x -> (x.getKey().scope.startsWith(scope)
										&& (x.getKey().name.equals(receiver)
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					}

				} else {
					/*
					 * Only "RECEIVER" in specified scope can receive
					 * (scope equals)
					 */
					SISServer.mapping.entrySet().stream()
							.filter(x -> (x.getKey().scope.equals(scope)
									&& (x.getKey().name.equals(receiver)
											|| x.getKey().componentType == ComponentType.Debugger)
							&& x.getValue().encoder != null)).forEach(x -> {
								try {
									// re-route this message to each
									// qualified
									// component
									x.getValue().encoder.sendMsg(kvList);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									System.out.println("ERROR: Fail to send "
											+ kvList + ", abort subtask");
								}
							});
				}

			} else {
				System.out.println("No Receiver");
				if (broadcast != null && broadcast.equals("True")) {
					System.out.println("Broadcast!");
					/*
					 * all components in the hierarchy (up/down)
					 * (scope startsWith)
					 */
					 System.out.println("Direction: "+direction);
					if (direction != null && direction.equals("Up")) {
						System.out.println("Direction: Up");
						System.out.println(scope);
						System.out.println(scope.startsWith("SIS.Tien"));
						SISServer.mapping.entrySet().stream().forEach(x->{
							System.out.println(x.getKey().scope+"\t"+(x.getKey().componentType==ComponentType.Controller)+"\t"+scope.startsWith(x.getKey().scope));
						});
						SISServer.mapping.entrySet().stream()
								.filter(x -> (scope.startsWith(x.getKey().scope)
										&& (x.getKey().componentType == ComponentType.Monitor
												|| x.getKey().componentType == ComponentType.Controller
												|| x.getKey().componentType == ComponentType.Advertiser
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					} else if (direction != null && direction.equals("Down")) {
						SISServer.mapping.entrySet().stream()
								.filter(x -> (x.getKey().scope.startsWith(scope)
										&& (x.getKey().componentType == ComponentType.Monitor
												|| x.getKey().componentType == ComponentType.Controller
												|| x.getKey().componentType == ComponentType.Advertiser
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					}
				} else {
					/*
					 * All components in specified scope can receive
					 * (scope equals)
					 */
					SISServer.mapping.entrySet().stream()
							.filter(x -> (x.getKey().scope.equals(scope)
									&& (x.getKey().componentType == ComponentType.Monitor
											|| x.getKey().componentType == ComponentType.Controller
											|| x.getKey().componentType == ComponentType.Advertiser
											|| x.getKey().componentType == ComponentType.Debugger)
							&& x.getValue().encoder != null)).forEach(x -> {
								try {
									// re-route this message to each
									// qualified
									// component
									x.getValue().encoder.sendMsg(kvList);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									System.out.println("ERROR: Fail to send "
											+ kvList + ", abort subtask");
								}
							});
				}
			}
		}

		// no sender no distribution
	}



	static void VoteHandler(String scope, String sender, String receiver,
			String direction, String broadcast, KeyValueList kvList, VoterTable voterTable) {

		System.out.println("===========Received Vote================");
		KeyValueList conn = new KeyValueList();
		conn.putPair("MessageType", "Vote");
		conn.putPair("VoterPhone", kvList.getValue("VoterPhone"));
		conn.putPair("CandidateID", kvList.getValue("CandidateID"));
		conn.putPair("VoteSuccess", "True");


		SISServer.mapping.entrySet().stream()
								.filter(x -> (scope.startsWith(x.getKey().scope)
										&& (x.getKey().name.equals(receiver)
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(conn);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});

		if (sender != null && !sender.equals("")) {
			// a sender is required
			System.out.println("Sender: "+sender);
			if (receiver != null) {
				if (broadcast != null && broadcast.equals("True")) {

					/*
					 * all "RECEIVER" in the broadcast hierarchy can receive
					 * (scope startsWith)
					 */
					if (direction != null && direction.equals("Up")) {

						SISServer.mapping.entrySet().stream()
								.filter(x -> (scope.startsWith(x.getKey().scope)
										&& (x.getKey().name.equals(receiver)
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});

					} else if (direction != null && direction.equals("Down")) {

						SISServer.mapping.entrySet().stream()
								.filter(x -> (x.getKey().scope.startsWith(scope)
										&& (x.getKey().name.equals(receiver)
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					}

				} else {
					/*
					 * Only "RECEIVER" in specified scope can receive
					 * (scope equals)
					 */
					SISServer.mapping.entrySet().stream()
							.filter(x -> (x.getKey().scope.equals(scope)
									&& (x.getKey().name.equals(receiver)
											|| x.getKey().componentType == ComponentType.Debugger)
							&& x.getValue().encoder != null)).forEach(x -> {
								try {
									// re-route this message to each
									// qualified
									// component
									x.getValue().encoder.sendMsg(kvList);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									System.out.println("ERROR: Fail to send "
											+ kvList + ", abort subtask");
								}
							});
				}

			} else {
				System.out.println("No Receiver");
				if (broadcast != null && broadcast.equals("True")) {
					System.out.println("Broadcast!");
					/*
					 * all components in the hierarchy (up/down)
					 * (scope startsWith)
					 */
					 System.out.println("Direction: "+direction);
					if (direction != null && direction.equals("Up")) {
						System.out.println("Direction: Up");
						System.out.println(scope);
						System.out.println(scope.startsWith("SIS.Tien"));
						SISServer.mapping.entrySet().stream().forEach(x->{
							System.out.println(x.getKey().scope+"\t"+(x.getKey().componentType==ComponentType.Controller)+"\t"+scope.startsWith(x.getKey().scope));
						});
						SISServer.mapping.entrySet().stream()
								.filter(x -> (scope.startsWith(x.getKey().scope)
										&& (x.getKey().componentType == ComponentType.Monitor
												|| x.getKey().componentType == ComponentType.Controller
												|| x.getKey().componentType == ComponentType.Advertiser
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					} else if (direction != null && direction.equals("Down")) {
						SISServer.mapping.entrySet().stream()
								.filter(x -> (x.getKey().scope.startsWith(scope)
										&& (x.getKey().componentType == ComponentType.Monitor
												|| x.getKey().componentType == ComponentType.Controller
												|| x.getKey().componentType == ComponentType.Advertiser
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					}
				} else {
					/*
					 * All components in specified scope can receive
					 * (scope equals)
					 */
					SISServer.mapping.entrySet().stream()
							.filter(x -> (x.getKey().scope.equals(scope)
									&& (x.getKey().componentType == ComponentType.Monitor
											|| x.getKey().componentType == ComponentType.Controller
											|| x.getKey().componentType == ComponentType.Advertiser
											|| x.getKey().componentType == ComponentType.Debugger)
							&& x.getValue().encoder != null)).forEach(x -> {
								try {
									// re-route this message to each
									// qualified
									// component
									x.getValue().encoder.sendMsg(kvList);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									System.out.println("ERROR: Fail to send "
											+ kvList + ", abort subtask");
								}
							});
				}
			}
		}

		// no sender no distribution
	}


	static void EmergencyHandler(String scope, String sender, String receiver,
			String direction, String broadcast, KeyValueList kvList) {

		KeyValueList conn = new KeyValueList();
			conn.putPair("MessageType", "Emergency");
			conn.putPair("Purpose", "Voting Emergency");
			conn.putPair("Vote", "Corrupted");

			SISServer.mapping.entrySet().stream()
								.filter(x -> (scope.startsWith(x.getKey().scope)
										&& (x.getKey().name.equals(receiver)
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(conn);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
		if (sender != null && !sender.equals("")) {
			// a sender is required

			if (receiver != null) {
				if (broadcast != null && broadcast.equals("True")) {

					/*
					 * all "RECEIVER" in the broadcast hierarchy can receive
					 * (scope startsWith)
					 */
					if (direction != null && direction.equals("Up")) {

						SISServer.mapping.entrySet().stream()
								.filter(x -> (scope.startsWith(x.getKey().scope)
										&& (x.getKey().name.equals(receiver)
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});

					} else if (direction != null && direction.equals("Down")) {

						SISServer.mapping.entrySet().stream()
								.filter(x -> (x.getKey().scope.startsWith(scope)
										&& (x.getKey().name.equals(receiver)
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					}

				} else {
					/*
					 * Only "RECEIVER" in specified scope can receive
					 * (scope equals)
					 */
					SISServer.mapping.entrySet().stream()
							.filter(x -> (x.getKey().scope.equals(scope)
									&& (x.getKey().name.equals(receiver)
											|| x.getKey().componentType == ComponentType.Debugger)
							&& x.getValue().encoder != null)).forEach(x -> {
								try {
									// re-route this message to each
									// qualified
									// component
									x.getValue().encoder.sendMsg(kvList);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									System.out.println("ERROR: Fail to send "
											+ kvList + ", abort subtask");
								}
							});
				}

			} else {
				if (broadcast != null && broadcast.equals("True")) {
					/*
					 * all components in the hierarchy (up/down)
					 * (scope startsWith)
					 */
					if (direction != null && direction.equals("Up")) {
						SISServer.mapping.entrySet().stream()
								.filter(x -> (scope.startsWith(x.getKey().scope)
										&& (x.getKey().componentType == ComponentType.Advertiser
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					} else if (direction != null && direction.equals("Down")) {
						SISServer.mapping.entrySet().stream()
								.filter(x -> (x.getKey().scope.startsWith(scope)
										&& (x.getKey().componentType == ComponentType.Advertiser
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					}
				} else {
					/*
					 * All components in specified scope can receive
					 * (scope equals)
					 */
					SISServer.mapping.entrySet().stream()
							.filter(x -> (x.getKey().scope.equals(scope)
									&& (x.getKey().componentType == ComponentType.Advertiser
											|| x.getKey().componentType == ComponentType.Debugger)
							&& x.getValue().encoder != null)).forEach(x -> {
								try {
									// re-route this message to each
									// qualified
									// component
									x.getValue().encoder.sendMsg(kvList);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									System.out.println("ERROR: Fail to send "
											+ kvList + ", abort subtask");
								}
							});
				}
			}
		}

		// no sender no distribution
	}


	static void TerminateHandler(String scope, String sender, String receiver,
			String direction, String broadcast, KeyValueList kvList, VoterTable voterTable) {

		System.out.println("===========Terminated Voting================");
		KeyValueList conn = new KeyValueList();

		HashMap<String, Integer> votes = voterTable.getResults();
		Set set = votes.entrySet();
	    Iterator iterator = set.iterator();
	    while(iterator.hasNext()) {
	         Map.Entry mentry = (Map.Entry)iterator.next();
	         System.out.print("CandidateId: "+ mentry.getKey() + " Votes: ");
	         System.out.println(mentry.getValue());

	         conn.putPair((String)mentry.getKey(), ((Integer)mentry.getValue()).toString());
	    }
	


				SISServer.mapping.entrySet().stream()
								.filter(x -> (scope.startsWith(x.getKey().scope)
										&& (x.getKey().name.equals(receiver)
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(conn);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});

	}

	static void SettingHandler(String scope, String sender, String receiver,
			String direction, String broadcast, KeyValueList kvList) {

		if (sender != null && !sender.equals("")) {
			// a sender is required

			if (receiver != null) {
				if (broadcast != null && broadcast.equals("True")) {

					/*
					 * all "RECEIVER" in the broadcast hierarchy can receive
					 * (scope startsWith)
					 */
					if (direction != null && direction.equals("Up")) {

						SISServer.mapping.entrySet().stream()
								.filter(x -> (scope.startsWith(x.getKey().scope)
										&& (x.getKey().name.equals(receiver)
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});

					} else if (direction != null && direction.equals("Down")) {

						SISServer.mapping.entrySet().stream()
								.filter(x -> (x.getKey().scope.startsWith(scope)
										&& (x.getKey().name.equals(receiver)
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					}

				} else {
					/*
					 * Only "RECEIVER" in specified scope can receive
					 * (scope equals)
					 */
					SISServer.mapping.entrySet().stream()
							.filter(x -> (x.getKey().scope.equals(scope)
									&& (x.getKey().name.equals(receiver)
											|| x.getKey().componentType == ComponentType.Debugger)
							&& x.getValue().encoder != null)).forEach(x -> {
								try {
									// re-route this message to each
									// qualified
									// component
									x.getValue().encoder.sendMsg(kvList);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									System.out.println("ERROR: Fail to send "
											+ kvList + ", abort subtask");
								}
							});
				}

			} else {
				if (broadcast != null && broadcast.equals("True")) {
					/*
					 * all components in the hierarchy (up/down)
					 * (scope startsWith)
					 */
					if (direction != null && direction.equals("Up")) {
						SISServer.mapping.entrySet().stream()
								.filter(x -> (scope.startsWith(x.getKey().scope)
										&& (x.getKey().componentType == ComponentType.Basic
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					} else if (direction != null && direction.equals("Down")) {
						SISServer.mapping.entrySet().stream()
								.filter(x -> (x.getKey().scope.startsWith(scope)
										&& (x.getKey().componentType == ComponentType.Basic
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					}
				} else {
					/*
					 * All components in specified scope can receive
					 * (scope equals)
					 */
					SISServer.mapping.entrySet().stream()
							.filter(x -> (x.getKey().scope.equals(scope)
									&& (x.getKey().componentType == ComponentType.Basic
											|| x.getKey().componentType == ComponentType.Debugger)
							&& x.getValue().encoder != null)).forEach(x -> {
								try {
									// re-route this message to each
									// qualified
									// component
									x.getValue().encoder.sendMsg(kvList);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									System.out.println("ERROR: Fail to send "
											+ kvList + ", abort subtask");
								}
							});
				}
			}
		}

		// no sender no distribution
	}
	


}
