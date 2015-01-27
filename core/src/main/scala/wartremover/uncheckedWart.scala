package org.brianmckenna.wartremover

import scala.annotation.StaticAnnotation
import scala.annotation.meta.{setter, field, getter}

@getter @field @setter
class uncheckedWart extends StaticAnnotation