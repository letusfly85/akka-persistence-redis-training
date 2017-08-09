package letusfly85

import akka.persistence.{PersistentActor, SnapshotOffer}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Materializer}

case class BadmintonMember(id: Int, name: String, score: Int)

class BadmintonActor extends PersistentActor {
  final implicit val materializer: Materializer = ActorMaterializer(ActorMaterializerSettings(context.system))

  var member = BadmintonMember(1, "", 0)

  override def persistenceId = s"${getClass.getName}PersistentId=${member.id.toString}"

  override def receiveRecover: Receive = {
    case SnapshotOffer(_, snapshot: BadmintonMember) =>
      member = snapshot
  }

  override def receiveCommand: Receive = {
    case msg: String =>
      member = member.copy(score = member.score+1)
  }

}
