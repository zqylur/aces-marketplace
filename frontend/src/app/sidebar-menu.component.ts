import { Component } from '@angular/core';

@Component({
  selector: 'app-sidebar-menu',
  template: `
    <div class="page-sidebar fixedscroll">

      <!-- MAIN MENU - START -->
      <div class="page-sidebar-wrapper" id="main-menu-wrapper">

        <ul class='wraplist' style="padding-top:1em">
          <li class="">
            <a routerLink="" routerLinkActive="active">Dashboard</a>
            <a href="index-socialmedia.html">
              <i class="fa fa-dashboard"></i>
              <span class="title">Dashboard</span>
            </a>
          </li>
          <li class="open">
            <a href="javascript:;">
              <i class="fa fa-gears"></i>
              <span class="title">Services</span>
              <span class="arrow "></span>
            </a>
            <ul class="sub-menu" >
              <li>
                <a class="" href="soc-mail-inbox.html" >Browse</a>
              </li>
              <li>
                <a class="" href="soc-mail-inbox.html" >
                  <span class="label label-accent">27</span>

                  New

                </a>
              </li>
              <li>
                <a class="" href="soc-mail-compose.html" >Recent</a>
              </li>
              <li>
                <a class="" href="soc-mail-compose.html" >Starred</a>
              </li>
            </ul>
          </li>
          <li class="">
            <a href="javascript:;">
              <i class="fa fa-file"></i>
              <span class="title">Contracts</span>
              <span class="arrow "></span><span class="label label-accent">4</span>
            </a>
            <ul class="sub-menu" >
              <li>
                <a class="" href="soc-mail-inbox.html" >Active</a>
                <span class="arrow "></span><span class="label label-accent">4</span>
              </li>
              <li>
                <a class="" href="soc-mail-compose.html" >Completed</a>
              </li>
              <li>
                <a class="" href="soc-mail-compose.html" >Failed</a>
              </li>
            </ul>
          </li>
          <li class="">
            <a href="javascript:;">
              <i class="fa fa-bar-chart"></i>
              <span class="title">Reports</span>
              <span class="arrow "></span>
            </a>
            <ul class="sub-menu" >
              <li>
                <a class="" href="soc-report-site.html" >Site</a>
              </li>
              <li>
                <a class="" href="soc-report-statistics.html" >Statistics</a>
              </li>
            </ul>
          </li>

          <li class="">
            <a href="javascript:;">
              <i class="fa fa-upload"></i>
              <span class="title">Deploy</span>
              <span class="arrow "></span>
            </a>
            <ul class="sub-menu" >
              <li>
                <a class="" href="soc-media.html" >Users</a>
              </li>
              <li>
                <a class="" href="soc-upload.html" >Security</a>
              </li>
              <li>
                <a class="" href="soc-upload.html" >ARK Wallet</a>
              </li>
              <li>
                <a class="" href="soc-upload.html" >Api Keys</a>
              </li>
            </ul>
          </li>

          <li class="">
            <a href="soc-account-settings.html">
              <i class="fa fa-bolt"></i>
              <span class="title">Funds</span>
            </a>
          </li>
          <li class="">
            <a href="soc-account-settings.html">
              <i class="fa fa-cogs"></i>
              <span class="title">Settings</span>
            </a>
          </li>
        </ul>

      </div>

    </div>
  `
})
export class SidebarMenuComponent {
}
