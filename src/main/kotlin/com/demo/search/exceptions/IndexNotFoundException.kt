package com.demo.search.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value=HttpStatus.NOT_FOUND)
class IndexNotFoundException() : RuntimeException() {}
