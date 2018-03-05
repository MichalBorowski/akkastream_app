package pl.softwareland.allegro.model

import java.util.Date

case class Repository(fullName:String, description:Option[String], cloneUrl:Option[String], start:Int=0, createdAt:Date)
