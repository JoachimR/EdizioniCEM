package de.reiss.edizioni

import java.io.File

data class ApkSignature(val storeFile: File,
                        val storePassword: String,
                        val keyAlias: String,
                        val keyPassword: String)