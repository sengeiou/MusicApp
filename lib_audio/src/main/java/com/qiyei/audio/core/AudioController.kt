/**
 * @author Created by qiyei2015 on 2020/4/6.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: AudioController
 */
package com.qiyei.audio.core

import android.content.Context
import android.util.Log
import androidx.core.location.LocationManagerCompat
import com.qiyei.audio.exception.AudioStatusException
import com.qiyei.audio.model.AudioBean
import kotlin.random.Random

class AudioController(private val mContext: Context) {

    companion object {
        const val TAG = "AudioController"
    }

    /**
     * 音频播放
     */
    private var mPlayer: AudioPlayer = AudioPlayer(mContext)

    /**
     * 歌曲队列
     */
    private var mQueue: MutableList<AudioBean> = mutableListOf()

    /**
     * 播放模式
     */
    private lateinit var mMode: PlayMode

    /**
     * 播放索引
     */
    private var mIndex: Int = 0;

    init {
        mMode = PlayMode.REPEAT
    }

    /**
     * 添加所有
     */
    fun addQueue(beans:List<AudioBean>){
        Log.i(TAG,"addQueue beans=$beans")
        mQueue.addAll(beans)
    }

    /**
     * 添加到播放队列中
     */
    fun addQueue(bean: AudioBean) {
        mQueue.add(bean)
    }

    /**
     * 删除队列中的歌曲
     */
    fun removeQueue(index:Int) {
        mQueue.removeAt(index)
    }

    /**
     * 清空播放列表
     */
    fun clearQueue(){
        mQueue.clear()
    }

    fun getQueue():MutableList<AudioBean>{
        return mQueue
    }

    /**
     * 获取播放模式
     */
    fun getPlayMode():PlayMode {
        return mMode
    }

    /**
     * 设置播放模式
     */
    fun setPlayMode(mode: PlayMode){
        mMode = mode
    }

    /**
     * 播放
     */
    fun play() {
        Log.i(TAG,"play bean=${mQueue[mIndex]}")
        mPlayer.load(mQueue[mIndex])
    }

    /**
     * 是否播放
     */
    fun isPlay():Boolean {
        return mPlayer.getStatus() == Status.STARTED
    }

    /**
     * 下一首
     */
    fun next() {
        val bean = getNextPlay()
        mPlayer.load(bean)
    }

    /**
     * 上一首
     */
    fun prev() {
        val bean = getPrevPlay()
        mPlayer.load(bean)
    }

    /**
     * 暂停
     */
    fun pause() {
        mPlayer.pause()
    }

    /**
     * 重新播放
     */
    fun resume() {
        mPlayer.resume()
    }


    fun getCurrentPlay(): AudioBean? {
        return mQueue[mIndex]
    }

    /**
     * 获取下一首要播放的
     */
    fun getNextPlay(): AudioBean {
        when (mMode) {
            PlayMode.REPEAT -> {
                mIndex = mIndex
            }

            PlayMode.LOOP -> {
                mIndex++
                if (mIndex == mQueue.size) {
                    mIndex = 0
                }
            }

            PlayMode.RANDOM -> {
                mIndex = Random.nextInt(0, mQueue.size)
            }
        }
        return mQueue[mIndex]
    }

    /**
     * 获取上一首播放
     */
    fun getPrevPlay(): AudioBean {
        when (mMode) {
            PlayMode.REPEAT -> {
                mIndex = mIndex
            }

            PlayMode.LOOP -> {
                mIndex--
                if (mIndex == -1) {
                    mIndex = mQueue.size - 1
                }
            }

            PlayMode.RANDOM -> {
                mIndex = Random.nextInt(0, mQueue.size)
            }
        }
        return mQueue[mIndex]
    }

    fun addAudioStatusListener(listener:IAudioStatusListener){
        mPlayer.addAudioStatusListener(listener)
    }

    fun removeAudioStatusListener(listener: IAudioStatusListener){
        mPlayer.removeAudioStatusListener(listener)
    }
}