package com.xubobo.composedemo

import android.os.Bundle
import android.text.style.LocaleSpan
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xubobo.composedemo.ui.theme.ComposeDemoTheme
import com.xubobo.composedemo.ui.theme.noto
import com.xubobo.composedemo.ui.theme.roboto

class CustomTextActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column {
                            Text(text = "hello你好1234567890", fontWeight = FontWeight.Normal)
                            Text(
                                text = "hello你好1234567890",
                                fontWeight = FontWeight.Normal,
                                fontFamily = noto
                            )
                            Text(text = "hello你好1234567890", fontWeight = FontWeight.SemiBold)
                            Text(
                                text = "hello你好1234567890",
                                fontWeight = FontWeight.Bold,
                                fontFamily = noto
                            )
                        }
                        Column {
                            Text(text = "1234567890")
                            Text(text = "1234567890", fontFamily = roboto)
                            Text(
                                text = "你好9",
                                fontFamily = roboto,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "hello你好9",
//                                fontFamily = roboto,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Text(
                                text = "hello你好9",
                                fontFamily = noto,
                            )
                        }
                        Column {
                            Text(
                                text = "1234567890",
                                modifier = Modifier.background(MaterialTheme.colorScheme.errorContainer),
                                fontSize = 20.sp
                            )
                            Text(
                                text = "1234567890",
                                fontFamily = roboto,
                                modifier = Modifier.background(MaterialTheme.colorScheme.errorContainer),
                                fontSize = 20.sp
                            )
                        }

                        Column {
                            Text(
                                text = "1111111119",
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.background(MaterialTheme.colorScheme.errorContainer),
                                fontSize = 20.sp
                            )
                            Text(
                                text = "1234567890",
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.background(MaterialTheme.colorScheme.errorContainer),
                                fontSize = 20.sp
                            )
                            Text(
                                text = "1234567890",
                                fontFamily = roboto,
                                modifier = Modifier.background(MaterialTheme.colorScheme.errorContainer),
                                fontSize = 20.sp
                            )
                        }

                        Column {
                            Text(
                                text = "1234567890",
                                style = LocalTextStyle.current.merge(TextStyle(fontSize = 20.sp))
                            )

                            Text(text = "1234567890", fontSize = 20.sp)
                            Text(
                                text = "1234567890",
                                style = TextStyle(fontFamily = roboto, fontSize = 20.sp)
                            )
                            Text(
                                text = "1234567890",
                                fontFamily = roboto,
                                fontSize = 20.sp
                            )
                        }

                        Column {
                            Text(
                                text = "你好",
                                style = LocalTextStyle.current.merge(TextStyle(fontSize = 25.sp)),
                                modifier = Modifier.background(MaterialTheme.colorScheme.errorContainer)
                            )

                            Text(
                                text = "你好", fontSize = 25.sp,
                                modifier = Modifier.background(MaterialTheme.colorScheme.errorContainer),
                            )


                            Text(
                                text = "你好",
                                fontFamily = roboto,
                                fontSize = 25.sp,
                                modifier = Modifier.background(MaterialTheme.colorScheme.errorContainer)
                            )
                            Text(
                                text = "你好",
                                style = LocalTextStyle.current.merge(
                                    TextStyle(
                                        fontFamily = roboto,
                                        fontSize = 25.sp
                                    )
                                ),
                                modifier = Modifier.background(MaterialTheme.colorScheme.errorContainer)
                            )
                            Text(
                                text = "你好",
                                style = TextStyle(fontFamily = roboto, fontSize = 25.sp),
                                modifier = Modifier.background(MaterialTheme.colorScheme.errorContainer)
                            )


                        }

                        Column {
                            Text(text = "1234567890", fontSize = 20.sp, fontFamily = roboto)
                            Text(text = buildAnnotatedString {
                                withStyle(SpanStyle(fontSize = 20.sp, fontFamily = roboto)) {
                                    append("1234567890")
                                }
                            })

                            Text(
                                text = buildAnnotatedString {

                                    withStyle(SpanStyle(fontSize = 20.sp)) {
                                        append("1234567890")
                                    }
                                },
                                modifier = Modifier.background(MaterialTheme.colorScheme.errorContainer)
                            )
                            Text(text = buildAnnotatedString {

                                withStyle(
                                    LocalTextStyle.current.merge(SpanStyle(fontSize = 20.sp))
                                        .toSpanStyle()
                                ) {
                                    append("1234567890")
                                }
                            }, modifier = Modifier.background(MaterialTheme.colorScheme.error))
                        }
                    }
                }
            }
        }
    }
}

