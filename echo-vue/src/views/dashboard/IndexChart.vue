<template>
  <div class="page-header-index-wide">
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="订单量" :total="8846 | NumberFormat">
          <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <mini-area />
          </div>
          <template slot="footer">订单总量<span> {{ '1234' | NumberFormat }}</span></template>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="拔丝明细" total="￥126,560">
          <a-tooltip title="说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <trend flag="up" style="margin-right: 16px;">
              <span slot="term">毛坯</span>
              12%
            </trend>
            <trend flag="down">
              <span slot="term">光球</span>
              11%
            </trend>
          </div>
          <template slot="footer">日均产量<span>￥ 234.56</span></template>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="毛坯明细" :total="6560 | NumberFormat">
          <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <mini-bar :height="40" />
          </div>
          <template slot="footer">日均产量 <span>60%</span></template>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="光球明细" total="78%">
          <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <mini-progress color="rgb(19, 194, 194)" :target="80" :percentage="78" :height="8" />
          </div>
          <template slot="footer">
            <trend flag="down" style="margin-right: 16px;">
              <span slot="term">周产量</span>
              12%
            </trend>
            <trend flag="up">
              <span slot="term">日产量</span>
              80%
            </trend>
          </template>
        </chart-card>
      </a-col>
    </a-row>

    <a-card :loading="loading" :bordered="false" :body-style="{ padding: '0' }">
      <div class="salesCard">
        <a-tabs default-active-key="1" size="large" :tab-bar-style="{ marginBottom: '24px', paddingLeft: '16px' }">
          <div class="extra-wrapper" slot="tabBarExtraContent">
            <div class="extra-item">
              <a>今日</a>
              <a>本周</a>
              <a>本月</a>
              <a>本年</a>
            </div>
            <a-range-picker :style="{ width: '256px' }" />
          </div>
          <a-tab-pane loading="true" tab="日历" key="1">
            <a-row>
              <a-calendar @panelChange="onPanelChange" @select="onSelect">
                <ul slot="dateCellRender" slot-scope="value" class="events">
                  <li v-for="item in getListData(value)" :key="item.id">
                    <a-badge :status="item.type" :text="item.content" />
                  </li>
                </ul>
                <template slot="monthCellRender" slot-scope="value">
                  <div v-if="getMonthData(value)" class="notes-month">
                    <section>{{ getMonthData(value) }}</section>
                    <span>Backlog number</span>
                  </div>
                </template>
              </a-calendar>
            </a-row>
          </a-tab-pane>
          <a-tab-pane tab="订单" key="2">
            <a-row>
              <a-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
                <bar title="送货趋势" :dataSource="barData" />
              </a-col>
              <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
                <rank-list title="毛坯产量排行" :list="rankList" />
              </a-col>
            </a-row>
          </a-tab-pane>
        </a-tabs>
      </div>
    </a-card>

    <a-row>
      <a-col :span="24">
        <a-card :loading="loading" :bordered="false" title="最近一周访问量统计" :style="{ marginTop: '24px' }">
          <a-row>
            <a-col :span="6">
              <head-info title="今日IP" :content="loginfo.todayIp"></head-info>
            </a-col>
            <a-col :span="2">
              <a-spin class='circle-cust'>
                <a-icon slot="indicator" type="environment" style="font-size: 24px" />
              </a-spin>
            </a-col>
            <a-col :span="6">
              <head-info title="今日访问" :content="loginfo.todayVisitCount"></head-info>
            </a-col>
            <a-col :span="2">
              <a-spin class='circle-cust'>
                <a-icon slot="indicator" type="team" style="font-size: 24px" />
              </a-spin>
            </a-col>
            <a-col :span="6">
              <head-info title="总访问量" :content="loginfo.totalVisitCount"></head-info>
            </a-col>
            <a-col :span="2">
              <a-spin class='circle-cust'>
                <a-icon slot="indicator" type="rise" style="font-size: 24px" />
              </a-spin>
            </a-col>
          </a-row>
          <line-chart-multid :fields="visitFields" :dataSource="visitInfo"></line-chart-multid>
        </a-card>
      </a-col>
    </a-row>
    <tally-flow-modal ref="flowForm" @ok="flowFormOk"></tally-flow-modal>
  </div>
</template>

<script>
import ChartCard from '@/components/ChartCard'
import ACol from "ant-design-vue/es/grid/Col"
import ATooltip from "ant-design-vue/es/tooltip/Tooltip"
import MiniArea from '@/components/chart/MiniArea'
import MiniBar from '@/components/chart/MiniBar'
import MiniProgress from '@/components/chart/MiniProgress'
import RankList from '@/components/chart/RankList'
import Bar from '@/components/chart/Bar'
import LineChartMultid from '@/components/chart/LineChartMultid'
import HeadInfo from '@/components/tools/HeadInfo.vue'

import Trend from '@/components/Trend'
import { getLoginfo, getVisitInfo } from '@/api/api'
import TallyFlowModal from '../modules/tally/flow/modules/TallyFlowModal'

const rankList = []
for (let i = 0; i < 7; i++) {
  rankList.push({
    name: '白鹭岛 ' + (i + 1) + ' 号店',
    total: 1234.56 - i * 100
  })
}
const barData = []
for (let i = 0; i < 12; i += 1) {
  barData.push({
    x: `${i + 1}月`,
    y: Math.floor(Math.random() * 1000) + 200
  })
}
export default {
  name: "IndexChart",
  components: {
    ATooltip,
    ACol,
    ChartCard,
    MiniArea,
    MiniBar,
    MiniProgress,
    RankList,
    Bar,
    Trend,
    LineChartMultid,
    HeadInfo,
    TallyFlowModal
  },
  data() {
    return {
      loading: true,
      center: null,
      rankList,
      barData,
      info: {},
      loginfo: {},
      visitFields: ['ip', 'visit'],
      visitInfo: [],
      flowList: [],
      indicator: <a-icon type="loading" style="font-size: 24px" spin />
    }
  },
  created() {
    setTimeout(() => {
      this.loading = !this.loading
    }, 1000)
    this.initLogInfo();
    this.loadDataByMonth()
  },
  methods: {
    initLogInfo() {
      getLoginfo(null).then((res) => {
        if (res.success) {
          Object.keys(res.result).forEach(key => {
            res.result[key] = res.result[key] + ""
          })
          this.loginfo = res.result;
        }
      })
      getVisitInfo().then(res => {
        if (res.success) {
          this.visitInfo = res.result;
        }
      })
    },
    onSelect(date) {
      console.log('onSelect', date.format('YYYY-MM-DD HH:mm:ss'));

      this.info.dateTime = date.format('YYYY-MM-DD HH:mm:ss');

      this.$refs.flowForm.add();
      this.$refs.flowForm.title = "新增";
      this.$refs.flowForm.info = this.info;
      this.$refs.flowForm.disableSubmit = false;
    },
    onPanelChange(value, mode) {
      console.log(value, mode);
    },
    getListData(value) {
      let datas;
      //遍历数组
      this.flowList.forEach(info => {
        //让数组的date与遍历到日历的那天的日期值相匹配
        if (info.date === value.format("YYYY-MM-DD")) {
          datas = info.listData;
        }
      });
      return datas;
    },
    loadDataByMonth(moth) {
      let monthDate = moth
      if (!moth) {
        monthDate = new Date().getMonth()
      }
      // 根据月份请求数据
      this.flowList = [
        {
          date: "2024-08-08",
          listData: [
            { type: "warning", content: "This is warning event." },
            { type: "success", content: "This is usual event." },
            { type: "error", content: "This is error event." },
          ],
        },
        {
          date: "2024-08-07",
          listData: [{ type: "warning", content: "This is warning event." }],
        },
        {
          date: "2024-08-06",
          listData: [
            { type: "warning", content: "This is warning event." },
            { type: "error", content: "This is error event." },
          ],
        },
      ]
    },
    flowFormOk() {

    },
  },
}
</script>

<style lang="less" scoped>
.circle-cust {
  position: relative;
  top: 28px;
  left: -100%;
}

.extra-wrapper {
  line-height: 55px;
  padding-right: 24px;

  .extra-item {
    display: inline-block;
    margin-right: 24px;

    a {
      margin-left: 24px;
    }
  }
}

/* 首页访问量统计 */
.head-info {
  position: relative;
  text-align: left;
  padding: 0 32px 0 0;
  min-width: 125px;

  &.center {
    text-align: center;
    padding: 0 32px;
  }

  span {
    color: rgba(0, 0, 0, .45);
    display: inline-block;
    font-size: .95rem;
    line-height: 42px;
    margin-bottom: 4px;
  }

  p {
    line-height: 42px;
    margin: 0;

    a {
      font-weight: 600;
      font-size: 1rem;
    }
  }
}
</style>