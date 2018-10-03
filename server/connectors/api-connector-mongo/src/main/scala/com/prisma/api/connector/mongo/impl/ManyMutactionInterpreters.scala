package com.prisma.api.connector.mongo.impl

import com.prisma.api.connector._
import com.prisma.api.connector.mongo.TopLevelDatabaseMutactionInterpreter
import com.prisma.api.connector.mongo.database.{MongoActionsBuilder, SequenceAction}
import com.prisma.gc_values.IdGCValue

import scala.concurrent.ExecutionContext

case class DeleteNodesInterpreter(mutaction: DeleteNodes)(implicit ec: ExecutionContext) extends TopLevelDatabaseMutactionInterpreter {

  def mongoAction(mutationBuilder: MongoActionsBuilder) =
    for {
      ids <- mutationBuilder.getNodeIdsByFilter2(mutaction.model, mutaction.whereFilter)
//      _   <- SequenceAction(checkForRequiredRelationsViolations(mutationBuilder, ids))
      _ <- mutationBuilder.deleteNodes(mutaction.model, ids)
    } yield MutactionResults(Vector(ManyNodesResult(mutaction, ids.size)))

  private def checkForRequiredRelationsViolations(mutationBuilder: MongoActionsBuilder, nodeIds: Vector[IdGCValue]) = {
//    val fieldsWhereThisModelIsRequired = mutaction.project.schema.fieldsWhereThisModelIsRequired(mutaction.model)
//    val actions                        = fieldsWhereThisModelIsRequired.map(field => mutationBuilder.errorIfNodesAreInRelation(nodeIds, field)).toVector
//
//    SequenceAction(actions)
    ???
  }
}

case class UpdateNodesInterpreter(mutaction: UpdateNodes)(implicit ec: ExecutionContext) extends TopLevelDatabaseMutactionInterpreter {

  def mongoAction(mutationBuilder: MongoActionsBuilder) =
    for {
      ids <- mutationBuilder.getNodeIdsByFilter2(mutaction.model, mutaction.whereFilter)
      _   <- mutationBuilder.updateNodes(mutaction, ids)
    } yield MutactionResults(Vector(ManyNodesResult(mutaction, ids.size)))
}

case class ResetDataInterpreter(mutaction: ResetData)(implicit ec: ExecutionContext) extends TopLevelDatabaseMutactionInterpreter {
  def mongoAction(mutationBuilder: MongoActionsBuilder) = {
    mutationBuilder.truncateTables(mutaction)
  }
}
