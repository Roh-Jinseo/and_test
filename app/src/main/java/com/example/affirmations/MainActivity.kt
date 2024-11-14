/*
 * 이 파일은 Affirmations 애플리케이션의 UI를 수정하여 3개의 컬럼으로 이미지를 배열하기 위한 변경 사항을 포함합니다.
 * LazyVerticalGrid를 사용하여 Affirmation 이미지와 텍스트를 3개씩 컬럼으로 표시합니다.
 */

package com.example.affirmations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.affirmations.R
import com.example.affirmations.data.Datasource
import com.example.affirmations.model.Affirmation
import com.example.affirmations.ui.theme.AffirmationsTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AffirmationsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AffirmationsApp()
                }
            }
        }
    }
}

@Composable
fun AffirmationsApp() {
    // 검색 바의 표시 상태를 기억합니다.
    var isSearchVisible by remember { mutableStateOf(false) }
    // 검색어 상태를 기억합니다.
    var searchQuery by remember { mutableStateOf("") }

    // 원본 데이터 로드
    val allAffirmations = Datasource().loadAffirmations()

    // 검색어에 따라 필터링된 데이터
    val filteredList = if (searchQuery.isEmpty()) {
        allAffirmations
    } else {
        allAffirmations.filter { affirmation ->
            LocalContext.current.getString(affirmation.stringResourceId)
                .contains(searchQuery, ignoreCase = true)
        }
    }

    Column {
        @OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
        Box {
            if (!isSearchVisible) {
                // 기본 TopAppBar
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.topbar)) },
                    actions = {
                        IconButton(onClick = {
                            isSearchVisible = true // 검색 버튼 클릭 시 검색 바 표시
                        }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Filled.Search,
                                contentDescription = "검색",
                                tint = Color.Gray
                            )
                        }
                    }
                )
            } else {
                // 검색 모드의 TopAppBar
                TopAppBar(
                    title = {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("검색") },
                            singleLine = true,
                            colors = androidx.compose.material3.TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            isSearchVisible = false
                            searchQuery = ""
                        }) {
                            Icon(
//                                imageVector = Icons.Default.ArrowBack,
                                imageVector = Icons.Default.KeyboardArrowLeft,
                                contentDescription = "뒤로가기"
                            )
                        }
                    },
                    actions = {}
                )
            }
        }

        AffirmationGrid(
            affirmationList = filteredList,
        )
    }
}

// LazyVerticalGrid를 사용하여 각 카드가 3개의 컬럼으로 배치되도록 구성합니다.
@Composable
fun AffirmationGrid(affirmationList: List<Affirmation>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // 3개의 고정 컬럼을 사용합니다.
        modifier = modifier
    ) {
        items(affirmationList) { affirmation ->
            AffirmationCard(
                affirmation = affirmation,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun AffirmationCard(affirmation: Affirmation, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column {
            Image(
                painter = painterResource(affirmation.imageResourceId),
                contentDescription = stringResource(affirmation.stringResourceId),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(194.dp),
                contentScale = ContentScale.Crop
            )
//            Text(
//                text = LocalContext.current.getString(affirmation.stringResourceId),
//                modifier = Modifier.padding(16.dp),
//                style = MaterialTheme.typography.headlineSmall
//            )
        }
    }
}

@Preview
@Composable
private fun AffirmationCardPreview() {
    AffirmationCard(Affirmation(R.string.affirmation1, R.drawable.image1))
}
