<script setup>
import { defineProps } from 'vue'

const props = defineProps({
  statusHistory: {
    type: Array,
    default: () => []
  }
})
</script>

<template>
  <div class="status-timeline">
    <h3>Historique des statuts</h3>
    <div v-if="statusHistory.length === 0" class="no-status">
      Aucun statut trouvé.
    </div>
    <div v-else class="timeline">
      <div
        v-for="(status, index) in statusHistory"
        :key="index"
        class="timeline-item"
      >
        <div class="timeline-marker"></div>
        <div class="timeline-content">
          <div class="date">{{ new Date(status.date).toLocaleDateString('fr-FR') }}</div>
          <div class="status">{{ status.statutLibelle }}</div>
          <div v-if="status.commentaire" class="comment">{{ status.commentaire }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.status-timeline {
  margin-top: 20px;
}

.timeline {
  position: relative;
  padding-left: 30px;
}

.timeline-item {
  position: relative;
  margin-bottom: 20px;
}

.timeline-marker {
  position: absolute;
  left: -40px;
  top: 0;
  width: 12px;
  height: 12px;
  background-color: #007bff;
  border-radius: 50%;
  border: 2px solid #fff;
  box-shadow: 0 0 0 2px #007bff;
}

.timeline-item:not(:last-child)::before {
  content: '';
  position: absolute;
  left: -35px;
  top: 12px;
  width: 2px;
  height: calc(100% + 8px);
  background-color: #007bff;
}

.timeline-content {
  background-color: #f8f9fa;
  padding: 10px;
  border-radius: 5px;
  border-left: 4px solid #007bff;
}

.date {
  font-weight: bold;
  color: #333;
}

.status {
  margin-top: 5px;
  font-size: 1.1em;
}

.comment {
  margin-top: 5px;
  font-style: italic;
  color: #666;
}

.no-status {
  color: #666;
  font-style: italic;
}
</style>
