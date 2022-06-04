package com.example.materialreviews.util

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts

class OpenDocumentWithPermissions : ActivityResultContracts.OpenDocument() {

    override fun createIntent(context: Context, input: Array<String>): Intent {
        return super.createIntent(context, input).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }


}