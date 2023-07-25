package semir.mahovkic.mahala.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import semir.mahovkic.mahala.R

const val Male = "M"
const val Female = "F"

@Composable
fun CandidateCard(
    profileImg: String?,
    profileImgSize: Dp,
    name: String,
    votingNumber: Int,
    party: String,
    gender: String,
    cardHeight: Dp,
    onCandidateClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
            .clickable {
                onCandidateClick()
            }
    ) {
        ProfileImage(profileImg, gender, profileImgSize)

        Spacer(modifier = Modifier.width(10.dp))

        CandidateInfo(
            name,
            votingNumber,
            party,
            cardHeight,
            Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun CandidateInfo(name: String, votingNumber: Int, party: String, height: Dp, modifier: Modifier) {
    Box(
        modifier = modifier
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 2.dp,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = name,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(all = 4.dp)
                )

                Spacer(modifier = Modifier.height(height))

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Nr: $votingNumber",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(all = 4.dp)
                            .align(Alignment.BottomStart)
                    )
                    Text(
                        text = party,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(all = 5.dp)
                            .align(Alignment.BottomEnd),
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileImage(profileImg: String?, gender: String, size: Dp) {
    val img = if (profileImg?.isNotEmpty() == true) profileImg else ""

    Image(
        painter = (img.ifEmpty {
            painterResource(
                if (gender == Male) R.drawable.profile
                else R.drawable.profile_female
            )
        }) as Painter,
        contentDescription = "Candidate profile image",
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
    )
}