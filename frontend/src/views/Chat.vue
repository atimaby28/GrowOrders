<script setup>
import { ref, onMounted, onBeforeUnmount } from "vue";
import axios from "axios";
import { Stomp } from "@stomp/stompjs";

// 로그인 정보 예시
const buyerId = 1;

// 상태
const rooms = ref([]);
const selectedRoom = ref(null);
const messages = ref([]);
const newMessage = ref("");
let stompClient = null;

// 1️⃣ 구매자가 참여한 채팅방 조회
const fetchRooms = async () => {
  try {
    const res = await axios.get(`/chat/room/buyer/${buyerId}`);
    rooms.value = res.data;
  } catch (err) {
    console.error("채팅방 조회 실패", err);
  }
};

// 2️⃣ 채팅방 선택 → 메시지 조회 + WebSocket 연결
const selectRoom = async (room) => {
  selectedRoom.value = room;
  try {
    const res = await axios.get(`/chat/room/${room.roomId}/messages`);
    messages.value = res.data;
  } catch (err) {
    console.error("메시지 조회 실패", err);
  }

  connectWebSocket(room.roomId);
};

// 3️⃣ WebSocket 연결 및 구독
const connectWebSocket = (roomId) => {
  if (stompClient) stompClient.disconnect();

  const socket = new WebSocket("ws://localhost:8080/ws"); // 서버 WebSocket 엔드포인트
  stompClient = Stomp.over(socket);

  stompClient.connect({}, () => {
    stompClient.subscribe(`/topic/${roomId}`, (msg) => {
      const messageObj = JSON.parse(msg.body);
      messages.value.push(messageObj);
    });
  });
};

// 4️⃣ 메시지 전송
const sendMessage = () => {
  if (!newMessage.value.trim() || !selectedRoom.value) return;

  stompClient.send(`/app/${selectedRoom.value.roomId}`, {}, newMessage.value);
  newMessage.value = "";
};

// 마운트 시 채팅방 조회
onMounted(() => {
  fetchRooms();
});

// 언마운트 시 WebSocket 연결 해제
onBeforeUnmount(() => {
  if (stompClient) stompClient.disconnect();
});
</script>

<template>
  <div class="chat-container">
    <div class="room-list">
      <h3>채팅방</h3>
      <ul>
        <li v-for="room in rooms" :key="room.roomId" @click="selectRoom(room)">
          농부 {{ room.farmerId }}와의 채팅
        </li>
      </ul>
    </div>

    <div class="chat-area" v-if="selectedRoom">
      <h3>채팅방: {{ selectedRoom.roomId }}</h3>
      <div class="messages">
        <div v-for="msg in messages" :key="msg.senderId + msg.message">
          <strong>{{ msg.senderName }}:</strong> {{ msg.message }}
        </div>
      </div>
      <input v-model="newMessage" placeholder="메시지 입력" @keyup.enter="sendMessage" />
      <button @click="sendMessage">전송</button>
    </div>
  </div>
</template>

<style scoped>
.chat-container {
  display: flex;
  gap: 20px;
}
.room-list {
  width: 200px;
  border-right: 1px solid #ccc;
  padding: 10px;
}
.chat-area {
  flex: 1;
  display: flex;
  flex-direction: column;
}
.messages {
  flex: 1;
  height: 400px;
  overflow-y: auto;
  border: 1px solid #ccc;
  margin-bottom: 10px;
  padding: 5px;
}
</style>
