package semir.mahovkic.mahala.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import semir.mahovkic.mahala.R

const val Male = "M"
const val Female = "F"

@Composable
fun ProfileImage(profileImg: String?, gender: String, size: Int = 100) {
    val img = if (profileImg?.isNotEmpty() == true) profileImg else ""


    Image(
        painter = (img.ifEmpty { painterResource(if (gender == Male) R.drawable.profile else R.drawable.profile_female) }) as Painter,
        contentDescription = "Candidate profile image",
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
    )
}
