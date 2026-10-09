package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.User
import com.example.ui.components.FuturisticButton
import com.example.ui.components.GlassmorphicCard
import com.example.ui.theme.*

@Composable
fun SettingsAndDocsScreen(
    currentUser: User,
    onSwitchUser: (asOwner: Boolean) -> Unit,
    onOpenAuthDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Current Account Card
        GlassmorphicCard(
            modifier = Modifier.fillMaxWidth(),
            elevation = 6.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "ACTIVE ACCOUNT SESSION",
                    color = SoftCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    if (currentUser.isOwner) listOf(ElectricViolet, NeonCyan) else listOf(SurfaceCardElevated, CyberBlue)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentUser.displayName.take(1).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentUser.displayName,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = if (currentUser.isOwner) DeepViolet else SurfaceDark
                            ) {
                                Text(
                                    text = if (currentUser.isOwner) "PLATFORM OWNER" else "FREE CREATOR",
                                    color = if (currentUser.isOwner) NeonCyan else TextSecondary,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(text = currentUser.email, color = TextSecondary, fontSize = 12.sp)

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (currentUser.isOwner)
                                "Unlimited Generation Quota • Full Administrative Privileges"
                            else
                                "${currentUser.freeCreditsRemaining} of ${currentUser.dailyQuotaLimit} free videos remaining today",
                            color = if (currentUser.isOwner) NeonGreen else SoftCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Switch Role / Account Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val targetOwner = !currentUser.isOwner
                            onSwitchUser(targetOwner)
                            Toast.makeText(
                                context,
                                if (targetOwner) "Switched to Platform Owner account (masabsajid8@gmail.com)" else "Switched to Free Creator account",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("toggle_account_role_btn"),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorderHighlight)
                    ) {
                        Icon(imageVector = Icons.Default.SwapHoriz, contentDescription = null, tint = SoftCyan, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (currentUser.isOwner) "Test as Free User" else "Test as Owner",
                            color = TextPrimary,
                            fontSize = 12.sp
                        )
                    }

                    Button(
                        onClick = onOpenAuthDialog,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("sign_in_custom_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceCardElevated)
                    ) {
                        Icon(imageVector = Icons.Default.Login, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Sign In As...", fontSize = 12.sp)
                    }
                }
            }
        }

        // Seedance 2.5 Architecture Documentation
        GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "SEEDANCE 2.5 ENGINE SPECIFICATIONS",
                    color = SoftCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                DocSpecItem(
                    title = "Model Architecture",
                    description = "Multimodal Temporal Latent Diffusion Transformer (DiT). Employs 3D spatio-temporal self-attention layers to guarantee continuous kinetic coherence across successive video frames without morphing artifacts."
                )

                DocSpecItem(
                    title = "Motion Transfer Engine",
                    description = "Extracts high-order skeletal keypoints and optical motion vectors from input references to transfer dynamic dance choreography and complex cinematic movements onto synthesized characters."
                )

                DocSpecItem(
                    title = "Cinematic Rendering Pipeline",
                    description = "Native support for anamorphic 2.39:1 Cinemascope, 16:9 widescreen, and 9:16 mobile formats. Output stream is post-processed through a neural color grading tensor with physically based depth-of-field."
                )

                DocSpecItem(
                    title = "Frame Rates & Durations",
                    description = "Generates 24-60 fps smooth playback. Variable sequence lengths of 4s, 6s, and 10s with seamless looping keyframe options."
                )
            }
        }

        // Free Access & Fair-Use Architecture
        GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "FREE GENERATION ARCHITECTURE",
                    color = SoftCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "How MotionVerse AI Operates Freely:",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "• 100% Free Public Access: No credit card or paid subscription required to generate high-resolution video clips.\n\n" +
                           "• Daily Fair-Use Quotas: Every registered user receives a refreshed batch of free video generations daily, preventing server exhaustion and runaway compute costs.\n\n" +
                           "• Owner-Configured Providers: The platform owner can connect authorized Seedance 2.5 API clusters or host open-source GPU models (such as CogVideoX) to power community generations.\n\n" +
                           "• Real Job Queue: Generations are prioritized and processed through asynchronous background workers with live stage tracking and cancellation support.",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
private fun DocSpecItem(title: String, description: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(NeonCyan)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = description,
            color = TextSecondary,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            modifier = Modifier.padding(start = 14.dp)
        )
    }
}
