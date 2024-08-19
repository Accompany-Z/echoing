<template>
  <div ref="vantaRef" style="width:100%;height:100vh">
    <div id="userLayout" :class="['user-layout-wrapper', device]">
      <div class="container">
        <route-view></route-view>
        <div class="footer">
          <div class="links">
            <a href="http://www.baidu.com" target="_blank">帮助</a>
            <a href="https://www.zwfcyy.com" target="_blank">隐私</a>
            <a href="https://www.zwfcyy.com" target="_blank">条款</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import RouteView from "@/components/layouts/RouteView"
import { mixinDevice } from '@/utils/mixin.js'
import * as THREE from 'three'
import RINGS from 'vanta/src/vanta.rings'
export default {
  name: "UserLayout",
  components: { RouteView },
  mixins: [mixinDevice],
  data() {
    return {
    }
  },
  mounted() {
    this.vantaEffect = RINGS({
      el: this.$refs.vantaRef,
      THREE: THREE
    })

    VANTA.RINGS({
      el: this.$refs.vantaRef,
      mouseControls: true,
      touchControls: true,
      gyroControls: false,
      minHeight: 200.00,
      minWidth: 200.00,
      scale: 1.00,
      scaleMobile: 1.00
    })
    document.body.classList.add('userLayout')
  },
  beforeDestroy() {
    if (this.vantaEffect) {
      this.vantaEffect.destroy()
    }
    document.body.classList.remove('userLayout')
  },
}
</script>

<style lang="less" scoped>
#userLayout.user-layout-wrapper {
  height: 100%;
  user-select: none;

  &.mobile {
    .container {
      display: flex;
      align-items: center;

      .main {
        max-width: 368px;
        width: 98%;
      }
    }
  }

  .container {
    width: 100%;
    min-height: 100%;
    background-size: 100%;
    position: relative;
    display: flex;
    align-items: center;

    a {
      text-decoration: none;
    }

    .main {
      min-width: 260px;
      width: 368px;
      margin: 0 auto;
      margin-left: 8%;
    }

    .footer {
      position: absolute;
      width: 100%;
      bottom: 0;
      padding: 0 16px;
      margin: 48px 0 0;
      text-align: center;

      .links {
        margin-bottom: 8px;
        font-size: 14px;

        a {
          color: rgba(255, 255, 255, 0.45);
          transition: all 0.3s;

          &:not(:last-child) {
            margin-right: 40px;
          }
        }
      }

      .copyright {
        color: rgba(0, 0, 0, 0.45);
        font-size: 14px;
      }
    }
  }
}
</style>